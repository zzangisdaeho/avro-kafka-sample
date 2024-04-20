package org.example.avrokafakasample.kafka;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class AvroDeserializer<T extends SpecificRecord> implements Deserializer<T> {

    @Override
    public T deserialize(String topic, byte[] data) {
        return deserialize(topic, null, data);
    }

    @Override
    public T deserialize(String topic, Headers headers, byte[] data) {
        if (data == null)
            return null;

        String targetTypeClassName = null;
        if (headers != null) {
            Header header = headers.lastHeader("AvroClassName");
            if (header != null) {
                targetTypeClassName = new String(header.value());
            }
        }

        if (targetTypeClassName == null) {
            throw new SerializationException("No target class specified for Avro deserialization");
        }

        Class<T> targetType;
        try {
            targetType = (Class<T>) Class.forName(targetTypeClassName);
        } catch (ClassNotFoundException e) {
            throw new SerializationException("Class for deserialization not found: " + targetTypeClassName, e);
        }

        DatumReader<T> reader = new SpecificDatumReader<>(targetType);
        try {
            BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(data, null);
            return reader.read(null, decoder);
        } catch (IOException e) {
            throw new SerializationException("Error deserializing Avro message", e);
        }
    }
}
