#!/bin/sh
echo "Please Login to OCP using oc login ..... "  
echo "Make sure Openshift Serverless Operator is installed"
echo "Make sure knative-serving namespace is created and an instance is already provisioned"
echo "Press [Enter] key to resume..." 
read

oc new-project dev
oc apply -f https://raw.githubusercontent.com/osa-ora/CustomerDataService/master/configs/db-secret.yaml
oc apply -f https://raw.githubusercontent.com/osa-ora/CustomerDataService/master/configs/customerdata-db-deployment.yaml
oc apply -f https://raw.githubusercontent.com/osa-ora/CustomerDataService/master/configs/customerdata-db-srv.yaml
echo "Service 'CustomerData-db' deployed successfully as ephemeral" 
echo "Login to CustomerData-db mysql pod and install the schema using:"
echo "mysql -u root"
echo "connect customers"
curl https://raw.githubusercontent.com/osa-ora/CustomerDataService/master/script/initial_customer_schema.sql

oc apply -f https://raw.githubusercontent.com/osa-ora/CustomerDataService/master/configs/image.yaml
oc apply -f https://raw.githubusercontent.com/osa-ora/CustomerDataService/master/configs/build-config.yaml
oc apply -f https://raw.githubusercontent.com/osa-ora/CustomerDataService/master/configs/customerdata-serverless.yaml
echo "Service 'CustomerDataService' deployed successfully as a serverless" 
echo "Completed!"
