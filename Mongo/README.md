# Multi-Tenant-Starbucks mongo cluster setup in AWS

## Prerequisite:

As a preparation for this setup, you should be able to launch an EC2 instance with a VPC and a public IP address, to ssh into it using a keypair, to install mongo inside.

## Steps to success:

##### step 1 - Prepare the instance:

- Create an EC2 instance with public ip address
- ssh into it using a pre-created key-value pair from AWS and install mongo

##### step 2 - Setup mongo configuration:

Stop the mongo if it is runing, and change configure file at the path /etc/mongod.conf with an editor of your choice (vim, nano, etc).

- Search for "net" entry, and comment out the sub entry "bindIP"

example:
![alt tag](https://github.com/azhadm/multi-tenant-starbucks/blob/master/Mongo/step2.1.png)

- Search for "replication" entry. If it is being commented out, delete the comment tag to use it. Then add sub entry "replSetName" with the name you want. For example rs0

example:
![alt tag](https://github.com/azhadm/multi-tenant-starbucks/blob/master/Mongo/step2.1.png)

- save and exit.

##### step 3 - Setup mongo resplica set:

At this point, you might want to create an image (AMI) and launch 2 new instances from that image, or you can repeat step 1 and 2 twice. You should have at least 3 mongo modes to have a meaningful mongo replicaset.

- Start mongo service on all 3 instances

        sudo service mongod start

- Initiate the replicaset in 1 instance

        rs.initiate()

- By this time, your current mongo should shows itself as Primary. Then you need to add the other two mongo nodes by their ip address. you can use either public or private IP addresses, but using private ones requires all three nodes in the same VPC.

        rs.add("<a_mongo_node>")

- check if your setup is successful. A success setup should show the members field with 3 memebers

        rs.status()

example:
![alt tag](https://github.com/azhadm/multi-tenant-starbucks/blob/master/Mongo/step3.4.png)

- check with other node. Among 3 nodes, one should be Primary, and the rest are Secondary.

example:
![alt tag](https://github.com/azhadm/multi-tenant-starbucks/blob/master/Mongo/step3.5.png)

CONGRATULATION, YOU HAVE SUCCESSFULLY CREATED A MONGO 3 NODE CLUSTER

