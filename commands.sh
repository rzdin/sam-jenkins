#sam package 
aws cloudformation package --template-file template.yaml --s3-bucket rana-zia-sam-bucket2020 --output-template-file gen/template-generated.yaml

#aws cloudformation deploy 
aws cloudformation deploy --template-file gen/template-generated.yaml --stack-name aws-lambda --capabilities CAPABILITY_IAM 
