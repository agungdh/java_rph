-- mysqldump-php https://github.com/ifsnop/mysqldump-php
--
-- Host: 127.0.0.1	Database: laila
-- ------------------------------------------------------
-- Server version 	5.5.5-10.3.16-MariaDB
-- Date: Tue, 20 Aug 2019 22:52:58 +0200

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(150) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `nip` varchar(30) NOT NULL,
  `alamat` varchar(100) NOT NULL,
  `jenis_kelamin` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
SET autocommit=0;
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `admin` with 0 row(s)
--

--
-- Table structure for table `hasil_uji`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hasil_uji` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_sampel` int(11) NOT NULL,
  `id_jenis_pengujian` int(11) NOT NULL,
  `tanggal_uji` date NOT NULL,
  `hasil_uji` varchar(50) NOT NULL,
  `keterangan` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_jenis_pengujian` (`id_jenis_pengujian`),
  KEY `id_sampel` (`id_sampel`),
  CONSTRAINT `hasil_uji_ibfk_1` FOREIGN KEY (`id_jenis_pengujian`) REFERENCES `jenis_pengujian` (`id`),
  CONSTRAINT `hasil_uji_ibfk_2` FOREIGN KEY (`id_sampel`) REFERENCES `sampel` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hasil_uji`
--

LOCK TABLES `hasil_uji` WRITE;
/*!40000 ALTER TABLE `hasil_uji` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `hasil_uji` VALUES (1,4,1,'2019-08-01','Baik','aafasdfa'),(2,3,4,'2019-08-28','Baik','Kurang Baik');
/*!40000 ALTER TABLE `hasil_uji` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `hasil_uji` with 2 row(s)
--

--
-- Table structure for table `jenis_pengujian`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jenis_pengujian` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nama_jenis_pengujian` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jenis_pengujian`
--

LOCK TABLES `jenis_pengujian` WRITE;
/*!40000 ALTER TABLE `jenis_pengujian` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `jenis_pengujian` VALUES (1,'satu 2'),(4,'test');
/*!40000 ALTER TABLE `jenis_pengujian` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `jenis_pengujian` with 2 row(s)
--

--
-- Table structure for table `kode_sampel`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `kode_sampel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `jenis_sampel` varchar(50) NOT NULL,
  `kode` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kode_sampel`
--

LOCK TABLES `kode_sampel` WRITE;
/*!40000 ALTER TABLE `kode_sampel` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `kode_sampel` VALUES (1,'Daging','DG1'),(2,'Daging','DG2'),(3,'Kulit','KL1'),(4,'Kulit','KL2');
/*!40000 ALTER TABLE `kode_sampel` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `kode_sampel` with 4 row(s)
--

--
-- Table structure for table `sampel`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sampel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_kode_sampel` int(11) NOT NULL,
  `id_jenis_pengujian` int(11) NOT NULL,
  `no_formulir` varchar(50) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `alamat` varchar(50) NOT NULL,
  `kecamatan` varchar(50) NOT NULL,
  `tanggal_uji` date NOT NULL,
  `jumlah` int(11) NOT NULL,
  `kondisi` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_kode_sampel` (`id_kode_sampel`),
  KEY `id_jenis_pengujian` (`id_jenis_pengujian`),
  CONSTRAINT `sampel_ibfk_1` FOREIGN KEY (`id_kode_sampel`) REFERENCES `kode_sampel` (`id`),
  CONSTRAINT `sampel_ibfk_2` FOREIGN KEY (`id_jenis_pengujian`) REFERENCES `jenis_pengujian` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sampel`
--

LOCK TABLES `sampel` WRITE;
/*!40000 ALTER TABLE `sampel` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `sampel` VALUES (3,3,4,'1241 241','qwrqw qrqwr','asf asfasdfa ','Metro Barat','2019-08-02',2,'Kurang Baik'),(4,1,4,'wertwet','34545wrtewer','ttwertwert','Metro Pusat','2019-08-03',0,'Baik');
/*!40000 ALTER TABLE `sampel` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `sampel` with 2 row(s)
--

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on: Tue, 20 Aug 2019 22:52:58 +0200
