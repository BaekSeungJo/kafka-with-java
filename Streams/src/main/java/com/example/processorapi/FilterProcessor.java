package com.example.processorapi;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;

/**
 * Project : Producer-Consumer
 * Class: FilterProcessor
 * Created by baegseungjo on 2021/10/25
 * <p>
 * Description:
 */
public class FilterProcessor implements Processor<String, String> {

    private ProcessorContext context;

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public void process(String key, String value) {
        if(value.length() > 5) {
            context.forward(key, value);
        }

        context.commit();
    }

    @Override
    public void close() {

    }
}
