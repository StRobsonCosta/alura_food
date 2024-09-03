# Welcome to your CDK Java project!

This is a blank project for CDK development with Java.

The `cdk.json` file tells the CDK Toolkit how to execute your app.

It is a [Maven](https://maven.apache.org/) based project, so you can open this project with any Maven compatible Java IDE to build and run tests.

## Useful commands

 * `mvn package`     compile and run tests
 * `cdk ls`          list all stacks in the app
 * `cdk synth`       emits the synthesized CloudFormation template
 * `cdk deploy`      deploy this stack to your default AWS account/region
 * `cdk diff`        compare deployed stack with current state
 * `cdk docs`        open CDK documentation

Enjoy!

-- Passo a Passo AWS deste Projeto:
- Comandos:
cdk bootstrap
Criar Repositório privado no ECR e ver comando para trazer imagem (docker push 905418058817.dkr.ecr.us-east-1.amazonaws.com/img-pedidos-ms:latest)

aws s3 ls  (caso não tenha nada >>> aws s3 mb s3://cdk-hnb659fds-assets-905418058817-us-east-1)
aws s3 ls
cdk deploy Vpc
cdk deploy --parameters Rds:senha=12345678 Rds
cdk deploy Cluster
cdk deploy Service

Testar requisição no Postman

