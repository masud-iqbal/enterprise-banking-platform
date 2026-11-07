# enterprise-banking-platform
Enterprise Banking Platform built with Java, Spring Boot, React, Kafka and PostgreSQL. Demonstrating microservices architecture and enterprise engineering practices.

#
mvn clean install -DskipTests
docker compose up -d
docker compose ps 
docker compose logs customer-db	["database system is ready to accept connections"]
docker exec -it banking-customer-db psql -U banking_app -d banking_customer_db
SELECT current_database();
\q


CREATE ROLE banking_app WITH LOGIN PASSWORD 'password';
CREATE DATABASE banking_customer_db OWNER banking_app;
docker exec -it postgres psql -U banking_app -d banking_customer_db



	