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
echo "CREATE TABLE `customers`.`customer` (`id` INT NOT NULL AUTO_INCREMENT, `firstName` VARCHAR(45) NULL, `lastName` VARCHAR(45) NULL, `email` VARCHAR(45) NULL, `city` VARCHAR(45) NULL, `state` VARCHAR(45) NULL,`birthday` VARCHAR(45) NULL, PRIMARY KEY (`id`));"

echo "INSERT INTO `customers`.`customer` (`id`, `firstName`, `lastName`, `email`, `city`,`state`, `birthday`) VALUES ('1', 'Osama', 'Oransa', 'osa.ora@acme.com', 'Cairo','No', '2000-01-09');
echo "INSERT INTO `customers`.`customer` (`id`, `firstName`, `lastName`, `email`, `city`,`state`, `birthday`) VALUES ('2', 'Sameh', 'Ahmed', 'sa.or@acme.com', 'Dubai','Yes', '2001-02-20');

echo "CREATE TABLE `customers`.`customer_accounts` (`customer_id` INT NOT NULL,`account_no` VARCHAR(45) NOT NULL, `type` VARCHAR(45) NULL,PRIMARY KEY (`customer_id`, `account_no`));"

echo "INSERT INTO `customers`.`customer_accounts` (`customer_id`, `account_no`, `type`) VALUES ('1', '123456-1', '100');"
echo "INSERT INTO `customers`.`customer_accounts` (`customer_id`, `account_no`, `type`) VALUES ('1', '123456-2', '101');"
echo "INSERT INTO `customers`.`customer_accounts` (`customer_id`, `account_no`, `type`) VALUES ('2', '2323445-1', '100');"

oc apply -f https://raw.githubusercontent.com/osa-ora/CustomerDataService/master/configs/image.yaml
oc apply -f https://raw.githubusercontent.com/osa-ora/CustomerDataService/master/configs/build-config.yaml
oc apply -f https://raw.githubusercontent.com/osa-ora/CustomerDataService/master/configs/customerdata-serverless.yaml
echo "Service 'CustomerDataService' deployed successfully as a serverless" 
echo "Completed!"
