-- MySQL dump 10.13  Distrib 8.0.27, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: dbtest
-- ------------------------------------------------------
-- Server version	8.0.26

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `email` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `address` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `sex` varchar(10) NOT NULL,
  PRIMARY KEY (`email`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES ('barozzi966@gmail.com','Matteo','Scaccabarozzi','Via Risorgimento 15A, Barzanò','ciao','male'),('valentinacurcio96@gmail.com','Valentina','Curcio','Via Casnedi 32, Valmadrera','ciao','female');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `supplier` varchar(45) NOT NULL,
  `customer` varchar(45) NOT NULL,
  `costShipment` float NOT NULL,
  `subtotal` float NOT NULL COMMENT 'without shipment',
  `dateOrder` datetime DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `orders_id_uindex` (`id`),
  KEY `orders_customers_email_fk` (`customer`),
  CONSTRAINT `orders_customers_email_fk` FOREIGN KEY (`customer`) REFERENCES `customers` (`email`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (48,'039','valentinacurcio96@gmail.com',60,4320,'2021-08-28 11:46:58','Via Casnedi 32, Valmadrera'),(49,'039','barozzi966@gmail.com',60,4320,'2021-08-28 17:38:36','Via Risorgimento 15A, Barzanò'),(50,'12345','valentinacurcio96@gmail.com',180,1120,'2021-08-29 23:10:36','Via Casnedi 32, Valmadrera'),(51,'12345','barozzi966@gmail.com',180,5423,'2021-08-30 12:18:37','Via Risorgimento 15A, Barzanò'),(52,'12345','valentinacurcio96@gmail.com',180,749,'2021-09-01 14:15:42','Via Casnedi 32, Valmadrera'),(53,'12345','barozzi966@gmail.com',180,6512,'2021-09-03 13:56:00','Via Risorgimento 15A, Barzanò'),(54,'112233','valentinacurcio96@gmail.com',0,900,'2021-09-05 01:03:57','Via Casnedi 32, Valmadrera'),(55,'12345','barozzi966@gmail.com',180,9200,'2021-09-06 08:46:07','Via Risorgimento 15A, Barzanò'),(56,'112233','barozzi966@gmail.com',0,6300,'2021-10-13 13:26:36','Via Risorgimento 15A, Barzanò');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `code` int NOT NULL,
  `name` varchar(45) NOT NULL,
  `description` varchar(300) NOT NULL,
  `category` varchar(45) NOT NULL,
  `photo` varchar(45) NOT NULL,
  PRIMARY KEY (`code`),
  UNIQUE KEY `products_code_uindex` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'Ipad Air 4','Tablet ultima generazione','Tablet','112.jfif'),(4,'Iphone 12','Apple iPhone 12 è uno degli smartphone iOS più avanzati e completi che ci siano in circolazione. Dispone di un grande display da 6.1 pollici con una risoluzione di 2532x1170 pixel.','Smartphone','4.png'),(10,'Xiaomi MI 10 Lite','Xiaomi Mi 10 Lite è uno smartphone di fascia media con sistema operativo Android che non abbiamo esitato a chiamare best buy nella prima metà del 2020. Lo schermo è un godibilissimo 6,57 pollici AMOLED con risoluzione 1080 x 2400 pixel','Smartphone','10.jpg'),(29,'Monopattino Ducati','Monopattino in alluminio\r\n25 km/h di velocità massima\r\n35 km di autonomia\r\npneumatici tubeless\r\nfreni a disco\r\npeso 27 kg','Monopattino elettrico','29.jfif'),(98,'Huawei Matebook D14','HUAWEI MateBook D 14 Laptop, Full View 1080P FHD Ultrabook PC, Intel Core i5-10210U, RAM 16GB, SSD da 512GB, Sensore Impronte Digitali, Windows 10 Home, HUAWEI Share, Layout Italiano, Gray','Laptop','98.jpg'),(133,'LG UHD TV','LG 43UP78006LB 43 pollici Smart TV 4K Ultra HD NOVITÀ 2021 Wi-Fi Processore Quad Core 4K AI Sound','TV','133.jpg'),(234,'OnePlus Nord 5G - Grigio','Smartphone OnePlus con ricezione 5G. Versione grigio','Cellulare','234.jpg'),(432,'PlayStation 5','PlayStation 5 Sony bianca','Console','432.png'),(987,'Apple Watch 6','Smartwatch ultima generazione Apple. Cinturino oro','Smartwatch','987.jfif'),(34892,'MacBook Air 2','Portatile Apple anno 2019','Portatile','34892.jpg');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products_order`
--

DROP TABLE IF EXISTS `products_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products_order` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product` int NOT NULL,
  `orderID` int NOT NULL,
  `quantity` int NOT NULL,
  `price` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `products_order_orders_id_fk` (`orderID`),
  KEY `products_order_products_code_fk` (`product`),
  CONSTRAINT `products_order_orders_id_fk` FOREIGN KEY (`orderID`) REFERENCES `orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `products_order_products_code_fk` FOREIGN KEY (`product`) REFERENCES `products` (`code`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products_order`
--

LOCK TABLES `products_order` WRITE;
/*!40000 ALTER TABLE `products_order` DISABLE KEYS */;
INSERT INTO `products_order` VALUES (60,98,48,6,720),(61,98,49,6,720),(62,234,50,4,280),(63,987,51,2,839),(64,98,51,5,749),(65,98,52,1,749),(66,987,53,3,839),(67,432,53,5,799),(68,29,54,1,900),(69,987,55,4,800),(70,34892,55,6,1000),(71,29,56,7,900);
/*!40000 ALTER TABLE `products_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shipment_policy`
--

DROP TABLE IF EXISTS `shipment_policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shipment_policy` (
  `id` int NOT NULL AUTO_INCREMENT,
  `supplier` varchar(45) NOT NULL,
  `cost_shipment` float DEFAULT NULL,
  `min_articles` int DEFAULT '999999999',
  `max_articles` int DEFAULT NULL,
  `free_shipment` float DEFAULT NULL COMMENT 'Min articles for free shipment',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shipment_policy_id_uindex` (`id`),
  KEY `shipment_policy_suppliers_code_fk` (`supplier`),
  CONSTRAINT `shipment_policy_suppliers_code_fk` FOREIGN KEY (`supplier`) REFERENCES `suppliers` (`code`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shipment_policy`
--

LOCK TABLES `shipment_policy` WRITE;
/*!40000 ALTER TABLE `shipment_policy` DISABLE KEYS */;
INSERT INTO `shipment_policy` VALUES (21,'12345',NULL,999999999,NULL,10000),(22,'12345',100,45,120,0),(23,'12345',180,1,10,0),(24,'039',NULL,999999999,NULL,8000),(25,'12345',150,11,30,0),(26,'12345',120,31,44,0),(27,'039',30,1,4,0),(28,'039',60,5,7,0),(29,'039',70,8,15,0),(30,'987',50,1,8,0),(31,'987',25,9,19,0),(32,'987',10,20,100,0),(33,'987',NULL,999999999,NULL,10000),(34,'112233',100,1,100,0),(35,'112233',NULL,999999999,NULL,100);
/*!40000 ALTER TABLE `shipment_policy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier_catalogue`
--

DROP TABLE IF EXISTS `supplier_catalogue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier_catalogue` (
  `price` float NOT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `product` int NOT NULL COMMENT 'product code',
  `supplier` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `supplierCatalogue_id_uindex` (`id`),
  KEY `supplier_catalogue_products_code_fk` (`product`),
  KEY `supplierCatalogue_suppliers_code_fk` (`supplier`),
  CONSTRAINT `supplier_catalogue_products_code_fk` FOREIGN KEY (`product`) REFERENCES `products` (`code`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `supplierCatalogue_suppliers_code_fk` FOREIGN KEY (`supplier`) REFERENCES `suppliers` (`code`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier_catalogue`
--

LOCK TABLES `supplier_catalogue` WRITE;
/*!40000 ALTER TABLE `supplier_catalogue` DISABLE KEYS */;
INSERT INTO `supplier_catalogue` VALUES (600,1,1,'12345'),(1000,8,34892,'12345'),(280,9,234,'12345'),(839,10,987,'12345'),(799,11,432,'12345'),(749,12,98,'12345'),(720,17,98,'039'),(620,18,1,'987'),(270,19,234,'987'),(829,20,4,'039'),(300,21,10,'112233'),(800,22,987,'112233'),(900,23,29,'112233'),(399,24,133,'987');
/*!40000 ALTER TABLE `supplier_catalogue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `suppliers`
--

DROP TABLE IF EXISTS `suppliers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `suppliers` (
  `password` varchar(45) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `code` varchar(45) NOT NULL,
  `evaluation` int DEFAULT NULL,
  PRIMARY KEY (`code`),
  UNIQUE KEY `suppliers_email_uindex` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `suppliers`
--

LOCK TABLES `suppliers` WRITE;
/*!40000 ALTER TABLE `suppliers` DISABLE KEYS */;
INSERT INTO `suppliers` VALUES ('ciao','BellaZi SRL','039',3),('ciao','EuroStore SPA','112233',3),('ciao','TechStore SRL','12345',5),('ciao','Paperino SNC','338',2),('ciao','VendiLO','987',4);
/*!40000 ALTER TABLE `suppliers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-10-28  9:19:47
