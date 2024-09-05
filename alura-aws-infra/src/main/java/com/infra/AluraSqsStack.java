package com.infra;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.sqs.Queue;
import software.constructs.Construct;

public class AluraSqsStack extends Stack {
    private final Queue sqsQueue;

    public AluraSqsStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public AluraSqsStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        sqsQueue = Queue.Builder.create(this, "AluraQueue")
                .queueName("AluraQueue")
                .build();

        CfnOutput.Builder.create(this, "sqs-queue-url")
                .exportName("sqs-queue-url")
                .value(sqsQueue.getQueueUrl())
                .build();
    }

    public Queue getSqsQueue() {
        return sqsQueue;
    }
}
