-- MySQL dump 10.13  Distrib 8.0.44, for Linux (x86_64)
--
-- Host: localhost    Database: gym_manager_db
-- ------------------------------------------------------
-- Server version	8.0.44-0ubuntu0.24.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `funcionarios`
--

DROP TABLE IF EXISTS `funcionarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `funcionarios` (
  `id_funcionario` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `cpf` varchar(100) NOT NULL,
  `data_nascimento` varchar(10) DEFAULT NULL,
  `telefone` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `cargo` varchar(50) DEFAULT NULL,
  `data_admissao` varchar(10) DEFAULT NULL,
  `senha` varchar(255) NOT NULL,
  `caminho_foto` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_funcionario`),
  UNIQUE KEY `cpf` (`cpf`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `funcionarios`
--

LOCK TABLES `funcionarios` WRITE;
/*!40000 ALTER TABLE `funcionarios` DISABLE KEYS */;
INSERT INTO `funcionarios` VALUES (30,'Gizelle','11111111111','00/00/0000','(  )      -    ','','Gerente','22/22/2222','0000',NULL),(31,'Gizelle','22222222222','22/22/2222','(  )      -    ','','Recepcionista','00/00/0000','0000',NULL),(32,'Gizelle','00000000000','00/00/0000','(  )      -    ','','Instrutor','00/00/0000','0000',NULL),(33,'Mariana Silva','12345678901','1990-04-12','11987654321','mariana.silva@example.com','Recepcionista','2022-01-15','senha123',NULL),(34,'João Pereira','98765432100','1985-09-30','11999887766','joao.pereira@example.com','Instrutor','2021-03-10','senha123',NULL),(35,'Ana Costa','45678912355','1992-07-22','11988776655','ana.costa@example.com','Instrutora','2023-05-20','senha123',NULL),(36,'Carlos Mendes','32165498700','1988-01-05','11977665544','carlos.mendes@example.com','Gerente','2020-11-01','senha123',NULL),(37,'Fernanda Rocha','15975348622','1995-11-18','11966554433','fernanda.rocha@example.com','Nutricionista','2024-02-01','senha123',NULL),(38,'Rafael Almeida','25836914733','1991-03-25','11955443322','rafael.almeida@example.com','Instrutor','2023-08-12','senha123',NULL),(39,'Beatriz Ramos','75395184677','1996-12-09','11944332211','beatriz.ramos@example.com','Recepcionista','2022-09-05','senha123',NULL),(40,'Leonardo Duarte','85245612344','1984-05-14','11933221100','leonardo.duarte@example.com','Personal Trainer','2021-06-18','senha123',NULL),(41,'Patrícia Gomes','14725836911','1993-10-01','11922110099','patricia.gomes@example.com','Instrutora','2024-01-20','senha123',NULL),(42,'Thiago Barbosa','36925814722','1989-02-27','11911009988','thiago.barbosa@example.com','Administrador','2020-12-10','senha123',NULL);
/*!40000 ALTER TABLE `funcionarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_aluno`
--

DROP TABLE IF EXISTS `tb_aluno`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_aluno` (
  `id_aluno` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `cpf` varchar(15) NOT NULL,
  `data_nascimento` varchar(10) DEFAULT NULL,
  `telefone` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `caminho_foto` varchar(255) DEFAULT NULL,
  `status` varchar(20) DEFAULT 'Ativo',
  `data_matricula` varchar(10) DEFAULT NULL,
  `id_plano` int DEFAULT NULL,
  PRIMARY KEY (`id_aluno`),
  UNIQUE KEY `cpf` (`cpf`),
  KEY `id_plano` (`id_plano`),
  CONSTRAINT `tb_aluno_ibfk_1` FOREIGN KEY (`id_plano`) REFERENCES `tb_plano` (`id_plano`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_aluno`
--

LOCK TABLES `tb_aluno` WRITE;
/*!40000 ALTER TABLE `tb_aluno` DISABLE KEYS */;
INSERT INTO `tb_aluno` VALUES (62,'mario','12345678912','00/00/0000','(  )      -    ','',NULL,'Ativo','27/11/2025',1),(63,'Diego','21351000000','00/00/0000','(  )      -    ','',NULL,'Ativo','27/11/2025',1),(64,'Gabriel','23213123213','32/13/2131','(  )      -    ','',NULL,'Ativo','27/11/2025',1),(65,'Douglas','32131312313','32/13/1313','(  )      -    ','',NULL,'Ativo','27/11/2025',1),(66,'Caio','23131231312','32/12/3213','(  )      -    ','',NULL,'Ativo','27/11/2025',1),(67,'Lais','23131231315','32/12/3213','(  )      -    ','',NULL,'Ativo','27/11/2025',1),(69,'Douglas','53131231315','32/12/3213','(  )      -    ','',NULL,'Ativo','27/11/2025',1);
/*!40000 ALTER TABLE `tb_aluno` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_exercicio`
--

DROP TABLE IF EXISTS `tb_exercicio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_exercicio` (
  `id_exercicio` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `grupo_muscular` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_exercicio`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_exercicio`
--

LOCK TABLES `tb_exercicio` WRITE;
/*!40000 ALTER TABLE `tb_exercicio` DISABLE KEYS */;
INSERT INTO `tb_exercicio` VALUES (1,'Supino Reto','Peito'),(2,'Crucifixo','Peito'),(3,'Puxada Frontal','Costas'),(4,'Remada Curvada','Costas'),(5,'Agachamento Livre','Pernas'),(6,'Leg Press 45','Pernas'),(7,'Rosca Direta','Bíceps'),(8,'Tríceps Corda','Tríceps'),(9,'Esteira','Cardio'),(10,'Bike','Cardio'),(11,'Supino 45','Peito');
/*!40000 ALTER TABLE `tb_exercicio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_ficha`
--

DROP TABLE IF EXISTS `tb_ficha`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_ficha` (
  `id_ficha` int NOT NULL AUTO_INCREMENT,
  `id_aluno` int NOT NULL,
  `objetivo` varchar(100) DEFAULT NULL,
  `data_criacao` date DEFAULT NULL,
  `status` varchar(20) DEFAULT 'Ativa',
  `id_instrutor` int DEFAULT NULL,
  PRIMARY KEY (`id_ficha`),
  KEY `id_aluno` (`id_aluno`),
  CONSTRAINT `tb_ficha_ibfk_1` FOREIGN KEY (`id_aluno`) REFERENCES `tb_aluno` (`id_aluno`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_ficha`
--

LOCK TABLES `tb_ficha` WRITE;
/*!40000 ALTER TABLE `tb_ficha` DISABLE KEYS */;
INSERT INTO `tb_ficha` VALUES (3,62,'Hipertrofia geral','2025-11-27','Ativa',NULL),(4,63,'Emagrecimento','2025-11-27','Ativa',NULL),(5,64,'Força','2025-11-27','Ativa',NULL),(6,65,'Treino iniciante','2025-11-27','Ativa',NULL);
/*!40000 ALTER TABLE `tb_ficha` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_item_ficha`
--

DROP TABLE IF EXISTS `tb_item_ficha`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_item_ficha` (
  `id_item` int NOT NULL AUTO_INCREMENT,
  `id_ficha` int NOT NULL,
  `id_exercicio` int NOT NULL,
  `series` int DEFAULT NULL,
  `repeticoes` varchar(20) DEFAULT NULL,
  `tempo` varchar(50) DEFAULT NULL,
  `divisao` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id_item`),
  KEY `id_ficha` (`id_ficha`),
  KEY `id_exercicio` (`id_exercicio`),
  CONSTRAINT `tb_item_ficha_ibfk_1` FOREIGN KEY (`id_ficha`) REFERENCES `tb_ficha` (`id_ficha`),
  CONSTRAINT `tb_item_ficha_ibfk_2` FOREIGN KEY (`id_exercicio`) REFERENCES `tb_exercicio` (`id_exercicio`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_item_ficha`
--

LOCK TABLES `tb_item_ficha` WRITE;
/*!40000 ALTER TABLE `tb_item_ficha` DISABLE KEYS */;
INSERT INTO `tb_item_ficha` VALUES (9,3,1,4,'10',NULL,'Segunda'),(10,3,3,4,'12',NULL,'Segunda'),(11,3,5,3,'12',NULL,'Quarta'),(12,3,7,3,'10',NULL,'Quarta'),(13,4,9,1,NULL,'30 min','Segunda'),(14,4,10,1,NULL,'20 min','Segunda'),(15,4,5,3,'15',NULL,'Quarta'),(16,4,8,3,'12',NULL,'Quarta'),(17,5,11,5,'5',NULL,'Segunda'),(18,5,4,5,'5',NULL,'Segunda'),(19,5,6,4,'6',NULL,'Quarta'),(20,5,7,4,'6',NULL,'Quarta'),(21,6,1,3,'10',NULL,'Segunda'),(22,6,3,3,'10',NULL,'Segunda'),(23,6,9,1,NULL,'15 min','Quarta'),(24,6,10,1,NULL,'15 min','Quarta');
/*!40000 ALTER TABLE `tb_item_ficha` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_pagamento`
--

DROP TABLE IF EXISTS `tb_pagamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_pagamento` (
  `id_pagamento` int NOT NULL AUTO_INCREMENT,
  `id_aluno` int NOT NULL,
  `valor` decimal(10,2) NOT NULL,
  `data_vencimento` date NOT NULL,
  `data_pagamento` date DEFAULT NULL,
  `status` varchar(20) DEFAULT 'Pendente',
  `forma_pagamento` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_pagamento`),
  KEY `id_aluno` (`id_aluno`),
  CONSTRAINT `tb_pagamento_ibfk_1` FOREIGN KEY (`id_aluno`) REFERENCES `tb_aluno` (`id_aluno`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_pagamento`
--

LOCK TABLES `tb_pagamento` WRITE;
/*!40000 ALTER TABLE `tb_pagamento` DISABLE KEYS */;
INSERT INTO `tb_pagamento` VALUES (8,62,100.00,'2025-12-27','2025-11-27','Pago','Dinheiro'),(9,63,100.00,'2025-12-27',NULL,'Pendente',NULL),(10,64,100.00,'2025-12-27',NULL,'Pendente',NULL),(11,65,100.00,'2025-12-27',NULL,'Pendente',NULL),(12,66,100.00,'2025-12-27',NULL,'Pendente',NULL),(13,67,100.00,'2025-12-27',NULL,'Pendente',NULL),(14,69,100.00,'2025-12-27',NULL,'Pendente',NULL),(15,62,100.00,'2026-01-27','2025-11-27','Pago','Dinheiro'),(16,62,100.00,'2026-02-27',NULL,'Pendente',NULL);
/*!40000 ALTER TABLE `tb_pagamento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_plano`
--

DROP TABLE IF EXISTS `tb_plano`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_plano` (
  `id_plano` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `valor` decimal(10,2) NOT NULL,
  `duracao_meses` int NOT NULL DEFAULT '1',
  PRIMARY KEY (`id_plano`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_plano`
--

LOCK TABLES `tb_plano` WRITE;
/*!40000 ALTER TABLE `tb_plano` DISABLE KEYS */;
INSERT INTO `tb_plano` VALUES (1,'Mensal',100.00,1),(2,'Trimestral',270.00,3),(4,'Anual',900.00,12);
/*!40000 ALTER TABLE `tb_plano` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-27  1:17:30
