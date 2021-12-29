pipeline {
  agent any
 
  stages {
    stage('Install sam-cli') {
      steps {
        sh 'python3 -m venv venv && venv/bin/pip install aws-sam-cli'
        stash includes: '**/venv/**/*', name: 'venv'
      }
    }
    stage('build') {
      environment {
        STACK_NAME = 'sam-app-beta-stage'
        S3_BUCKET = 'sam-jenkins-demo-us-east-2-ranaziauddin'
      }
      steps {
        withAWS(credentials: 'AWSReservedSSO_AdministratorAccess_564bcbbbca5e5655/rzdin@enquizit.com', region: 'us-east-2') {
          unstash 'venv'
          unstash 'aws-sam'
          sh 'venv/bin/sam package --stack-name $STACK_NAME -t template.yaml --s3-bucket $S3_BUCKET --output-template-file gen/template-generated.yaml'
      }
    stage('beta') {
      environment {
        STACK_NAME = 'sam-app-beta-stage'
        S3_BUCKET = 'sam-jenkins-demo-us-east-2-ranaziauddin'
      }
      steps {
        withAWS(credentials: 'AWSReservedSSO_AdministratorAccess_564bcbbbca5e5655/rzdin@enquizit.com', region: 'us-east-2') {
          unstash 'venv'
          unstash 'aws-sam'
          sh 'venv/bin/sam deploy --stack-name $STACK_NAME -t template.yaml --s3-bucket $S3_BUCKET --capabilities CAPABILITY_IAM'
          }
        }
      }
    }


