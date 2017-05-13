# multi-tenant-starbucks

#Steps to create Kong cluster

# Create 4 EC2 t2.micro instances
![alt tag](https://github.com/azhadm/multi-tenant-starbucks/blob/master/KongSteps/Step1proj.png)

# Login to each instances using terminal
ssh -i <key> ec2-user@<ip address>

# Once you login to instance set up banner in .bash_profile
example:
![alt tag](https://github.com/azhadm/multi-tenant-starbucks/blob/master/KongSteps/proj:%20banner.png)

# Install docker in all the clusters and start it
(i) install docker:
      sudo yum intall docker
(ii) start docker:
      sudo service docker start  
      
# cas1 (First node of cassandra):
syntax:
docker run --name kongdb -d -e CASSANDRA_BROADCAST_ADDRESS=<ip of cas1> -e CASSANDRA_START_RPC=true -p 7000:7000 -p 7001:7001 -p 7199:7199 -p  9042:9042 -p 9160:9160 cassandra:3
eg: 
sudo docker run --name kongdb -d -e CASSANDRA_BROADCAST_ADDRESS=54.183.142.206 -e CASSANDRA_START_RPC=true -p 7000:7000 -p 7001:7001 -p 7199:7199 -p  9042:9042 -p 9160:9160 cassandra:3

# cas2 (Second node of cassandra):
syntax:
sudo docker run --name kongdb -d -e CASSANDRA_BROADCAST_ADDRESS=<ip of cas2> -e CASSANDRA_SEEDS=5<ip of cas1> -e CASSANDRA_START_RPC=true -p 7000:7000 -p 7001:7001 -p 7199:7199 -p 9042:9042 -p 9160:9160 cassandra:3
eg:
sudo docker run --name kongdb -d -e CASSANDRA_BROADCAST_ADDRESS=52.53.198.139 -e CASSANDRA_SEEDS=54.183.142.206 -e CASSANDRA_START_RPC=true -p 7000:7000 -p 7001:7001 -p 7199:7199 -p 9042:9042 -p 9160:9160 cassandra:3

# cas3 (Third node of cassandra):
syntax:
sudo docker run --name kongdb -d -e CASSANDRA_BROADCAST_ADDRESS=<ip of cas3> -e CASSANDRA_SEEDS=<ip of cas1>,<ip of cas2> -e CASSANDRA_START_RPC=true -p 7000:7000 -p 7001:7001 -p 7199:7199 -p 9042:9042 -p 9160:9160 cassandra:3
eg:
sudo docker run --name kongdb -d -e CASSANDRA_BROADCAST_ADDRESS=13.56.13.230 -e CASSANDRA_SEEDS=54.183.142.206,52.53.198.139 -e CASSANDRA_START_RPC=true -p 7000:7000 -p 7001:7001 -p 7199:7199 -p 9042:9042 -p 9160:9160 cassandra:3

# Kong Server
syntax:
sudo docker run -d --name kong -e "KONG_DATABASE=cassandra" -e "KONG_CASSANDRA_CONTACT_POINTS=<ip of cas1>,<ip of cas2>,<ip of cas3>" -e "KONG_PG_HOST=<ip of cas1>" -p 8000:8000 -p 8443:8443 -p 8001:8001 -p 7946:7946 -p 7946:7946/udp kong 
eg:
sudo docker run -d --name kong -e "KONG_DATABASE=cassandra" -e "KONG_CASSANDRA_CONTACT_POINTS=54.183.142.206,52.53.198.139,13.56.13.230" -e "KONG_PG_HOST=54.183.142.206" -p 8000:8000 -p 8443:8443 -p 8001:8001 -p 7946:7946 -p 7946:7946/udp kong

# Check status of nodes inside any cassandra node
sudo docker exec -it kongdb bash
nodetool status
![alt tag](https://github.com/azhadm/multi-tenant-starbucks/blob/master/KongSteps/proj:nodestatus.png)
