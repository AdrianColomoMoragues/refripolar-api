-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: bdrefripolarv1
-- ------------------------------------------------------
-- Server version	8.0.35

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
-- Table structure for table `categorias_profesionales`
--

DROP TABLE IF EXISTS `categorias_profesionales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorias_profesionales` (
  `codigo` varchar(5) NOT NULL,
  `descripcion` varchar(50) DEFAULT NULL,
  `encargo` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorias_profesionales`
--

LOCK TABLES `categorias_profesionales` WRITE;
/*!40000 ALTER TABLE `categorias_profesionales` DISABLE KEYS */;
INSERT INTO `categorias_profesionales` VALUES ('OP','Oficial de primera','obra'),('OS','Oficial de segunda','reparacion'),('OTO','Oficial de tercera','obra'),('OTR','Oficial de tercera','reparacion'),('PEO','Peon','obra'),('PER','Peon','reparacion');
/*!40000 ALTER TABLE `categorias_profesionales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clientes`
--

DROP TABLE IF EXISTS `clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clientes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `correo` varchar(50) DEFAULT NULL,
  `localidad` varchar(50) DEFAULT NULL,
  `direccion` varchar(100) DEFAULT NULL,
  `caracteristicas` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES (1,'Alimentos Sabrosos S.A.','987654321','info@alimentossabrosos.com','Madrid','Calle Principal 123','Productos gourmet'),(2,'Delicias Naturales S.L.','602324589','contacto@deliciasnaturales.es','Elche','Avenida Secundaria 456','Productos orgánicos'),(3,'Sabores Exquisitos C.B.','567890123','ventas@saboresexquisitos.com','Elche','Plaza Principal 789','Especialidades locales'),(4,'Sabor y Nutrición S.C.','321098765','info@saborynutricion.com','Orihuela','Calle de la Salud 234','Productos saludables'),(5,'Gastronomía Selecta S.A.','876543210','contacto@gastronomiaselecta.com','Denia','Paseo de la Gastronomía 567','Cocina gourmet'),(6,'Productos Deliciosos C.D.','109876543','ventas@productosdeliciosos.es','Monovar','Calle de los Sabores 890','Amplia variedad de productos');
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `empleados`
--

DROP TABLE IF EXISTS `empleados`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `empleados` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) DEFAULT NULL,
  `apellido_1` varchar(50) DEFAULT NULL,
  `apellido_2` varchar(50) DEFAULT NULL,
  `cod_categoriaProfesional` varchar(10) DEFAULT NULL,
  `antiguedad` int DEFAULT NULL,
  `id_asignado` int DEFAULT NULL,
  `reconocimiento_medico` tinyint(1) DEFAULT NULL,
  `direccion` varchar(100) DEFAULT NULL,
  `cp` int DEFAULT NULL,
  `mail` varchar(60) DEFAULT NULL,
  `ciudad` varchar(50) DEFAULT NULL,
  `provincia` varchar(50) DEFAULT NULL,
  `salario` decimal(10,2) DEFAULT NULL,
  `telefono` int DEFAULT NULL,
  `imagen` text,
  PRIMARY KEY (`id`),
  KEY `fk_categoriaProfesional` (`cod_categoriaProfesional`),
  KEY `fk_id_asignado` (`id_asignado`),
  CONSTRAINT `fk_categoriaProfesional` FOREIGN KEY (`cod_categoriaProfesional`) REFERENCES `categorias_profesionales` (`codigo`),
  CONSTRAINT `fk_id_asignado` FOREIGN KEY (`id_asignado`) REFERENCES `encargos` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `empleados`
--

LOCK TABLES `empleados` WRITE;
/*!40000 ALTER TABLE `empleados` DISABLE KEYS */;
INSERT INTO `empleados` VALUES (1,'Juan','García','López','OP',15,2,0,'Calle Principal, Nº 123',28001,'juan.garcia@example.com','Alicante','Alicante',2997.78,611223344,'https://colmor.blob.core.windows.net/2dam/img_avatar.png'),(2,'Pablo','Sánchez','Gómez','OTO',4,1,1,'Avenida del Parque, Nº 4563',28002,'pablo.sanchez@example.com','Alicante','Alicante',4661.73,622334455,'https://colmor.blob.core.windows.net/2dam/1733808.png'),(3,'Antonio','Pérez','Rodríguez','OP',7,NULL,1,'Calle Roble, Nº 789',28003,'antonio.perez@example.com','Alicante','Alicante',1315.32,633445566,'https://colmor.blob.core.windows.net/2dam/1785888.png'),(4,'Manuel','Martínez','López','OS',20,12,0,'Calle Alameda, Nº 101',28004,'manuel.martinez@example.com','Alicante','Alicante',3591.41,644556677,'https://colmor.blob.core.windows.net/2dam/Empleado4.png'),(5,'Francisco','López','Martínez','OP',16,1,1,'Calle del Bosque, Nº 222',28005,'francisco.lopez@example.com','Alicante','Alicante',1011.10,655667788,'https://colmor.blob.core.windows.net/2dam/6075806.png'),(6,'José','González','García','OS',7,NULL,1,'Calle del Jardín, Nº 333',28006,'jose.gonzalez@example.com','Alicante','Alicante',1281.25,666778899,'https://colmor.blob.core.windows.net/2dam/Empleado3.png'),(7,'Andrés','Fernández','Ruiz','OTR',3,12,0,'Calle del Río, Nº 444',28007,'andres.fernandez@example.com','Alicante','Alicante',2372.97,677889900,NULL),(8,'Miguel','Torres','Vargas','PEO',4,1,1,'Paseo del Lago, Nº 555',28008,'miguel.torres@example.com','Alicante','Alicante',3021.09,688990011,NULL),(9,'Luis','Ramírez','Medina','PEO',4,2,0,'Avenida del Sol, Nº 666',28009,'luis.ramirez@example.com','Alicante','Alicante',2986.53,699001122,NULL),(10,'Carlos','Herrera','Jiménez','OTO',7,1,0,'Plaza Mayor, Nº 777',28010,'carlos.herrera@example.com','Alicante','Alicante',4869.39,610112233,'https://colmor.blob.core.windows.net/2dam/Empleado2.png'),(11,'Unai','Larrañaga','Rodríguez','PEO',7,1,1,'Calle del Mar, Nº 888',28011,'unai.larranaga@example.com','Alicante','Alicante',2387.34,621122334,'https://colmor.blob.core.windows.net/2dam/8090406.png'),(12,'Mikel','Mendizábal','Torres','PER',2,11,0,'Avenida de la Luna, Nº 999',28012,'mikel.mendizabal@example.com','Alicante','Alicante',4328.54,632233445,'https://colmor.blob.core.windows.net/2dam/5335674.png'),(13,'Juan','Gómez','López','PER',2,NULL,0,'Calle de las Estrellas, Nº 000',28013,'juan.gomez@example.com','Alicante','Alicante',1480.67,643344556,'https://colmor.blob.core.windows.net/2dam/2810539.png'),(14,'Antonio','Fernández','Martínez','PER',3,12,0,'Calle de la Montaña, Nº 111',28014,'antonio.fernandez@example.com','Alicante','Alicante',1417.75,654455667,'https://colmor.blob.core.windows.net/2dam/1733808.png'),(15,'Miguel','Rodríguez','Sánchez','OS',12,11,0,'Avenida del Cielo, Nº 222',28015,'miguel.rodriguez@example.com','Alicante','Alicante',1646.75,665566778,NULL),(16,'Francisco','Gutiérrez','Pérez','OS',8,12,0,'Calle del Campo, Nº 333',28016,'francisco.gutierrez@example.com','Alicante','Alicante',2980.49,676677889,NULL),(21,'Carlos','Martínez','García','PEO',0,1,1,'Calle Inventada Nº2',3005,'carlosmartinez@correo.com','Alicante','Alicante',1234.00,312654390,NULL),(22,'David','González','Pérez','OTO',3,1,0,'Calle Imaginaria Nº3',23123,'davidgonzaper@example.es','Campello','Alicante',1200.00,421321221,NULL),(23,'María','Gómez','Díaz','OP',7,1,1,'Calle Abstracta Nº4',23122,'mariagodi@correo.es','Alicante','Alicante',1400.00,456543234,'https://colmor.blob.core.windows.net/2dam/23124321.jpg');
/*!40000 ALTER TABLE `empleados` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `encargos`
--

DROP TABLE IF EXISTS `encargos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `encargos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tipo` varchar(50) DEFAULT NULL,
  `nombre` varchar(100) DEFAULT NULL,
  `id_encargado` int DEFAULT NULL,
  `id_cliente` int DEFAULT NULL,
  `terminada` tinyint(1) DEFAULT NULL,
  `prioridad` int DEFAULT NULL,
  `porcentaje` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_idEncargado` (`id_encargado`),
  KEY `fk_idCliente` (`id_cliente`),
  CONSTRAINT `fk_idCliente` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id`),
  CONSTRAINT `fk_idEncargado` FOREIGN KEY (`id_encargado`) REFERENCES `empleados` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `encargos`
--

LOCK TABLES `encargos` WRITE;
/*!40000 ALTER TABLE `encargos` DISABLE KEYS */;
INSERT INTO `encargos` VALUES (1,'Obra','Obra Alimentos Sabrasos',1,1,1,3,100),(2,'Obra','Obra Gastronomía Selecta',2,5,0,1,90),(11,'Reparacion','Reparacion  Camara',7,4,0,1,90),(12,'Reparacion','Reparacion Tuberias',6,5,1,3,100);
/*!40000 ALTER TABLE `encargos` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-17 12:24:37
