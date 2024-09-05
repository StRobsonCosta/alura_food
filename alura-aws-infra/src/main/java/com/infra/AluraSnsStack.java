package com.infra;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.sns.Topic;
import software.constructs.Construct;

public class AluraSnsStack extends Stack {
    private final Topic snsTopic;

    public AluraSnsStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public AluraSnsStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        snsTopic = Topic.Builder.create(this, "AluraTopic")
                .displayName("Alura SNS Topic")
                .build();

        CfnOutput.Builder.create(this, "sns-topic-arn")
                .exportName("sns-topic-arn")
                .value(snsTopic.getTopicArn())
                .build();
    }

    public Topic getSnsTopic() {
        return snsTopic;
    }
}
