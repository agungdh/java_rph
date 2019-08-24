-- mysqldump-php https://github.com/ifsnop/mysqldump-php
--
-- Host: 127.0.0.1	Database: laila
-- ------------------------------------------------------
-- Server version 	5.5.5-10.3.16-MariaDB
-- Date: Sat, 24 Aug 2019 11:51:37 +0200

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `admin` VALUES (1,'admin','21232f297a57a5a743894a0e4a801fc3','admin','admin','admin','admin');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `admin` with 1 row(s)
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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hasil_uji`
--

LOCK TABLES `hasil_uji` WRITE;
/*!40000 ALTER TABLE `hasil_uji` DISABLE KEYS */;
SET autocommit=0;
/*!40000 ALTER TABLE `hasil_uji` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `hasil_uji` with 0 row(s)
--

--
-- Table structure for table `jenis_pengujian`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jenis_pengujian` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_sampel` int(11) DEFAULT NULL,
  `nama_jenis_pengujian` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_sampel` (`id_sampel`),
  CONSTRAINT `jenis_pengujian_ibfk_1` FOREIGN KEY (`id_sampel`) REFERENCES `sampel` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jenis_pengujian`
--

LOCK TABLES `jenis_pengujian` WRITE;
/*!40000 ALTER TABLE `jenis_pengujian` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `jenis_pengujian` VALUES (11,9,'Bau'),(12,10,'Reduktase'),(13,9,'CMT'),(14,9,'Warna');
/*!40000 ALTER TABLE `jenis_pengujian` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `jenis_pengujian` with 4 row(s)
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kode_sampel`
--

LOCK TABLES `kode_sampel` WRITE;
/*!40000 ALTER TABLE `kode_sampel` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `kode_sampel` VALUES (6,'Daging','DG'),(7,'Kulit','KL'),(8,'MIE AYAM','MA');
/*!40000 ALTER TABLE `kode_sampel` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `kode_sampel` with 3 row(s)
--

--
-- Table structure for table `sampel`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sampel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_kode_sampel` int(11) NOT NULL,
  `no_kode` int(11) NOT NULL,
  `no_formulir` varchar(50) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `alamat` varchar(50) NOT NULL,
  `kecamatan` varchar(50) NOT NULL,
  `tanggal_sampel` date NOT NULL,
  `jumlah` int(11) NOT NULL,
  `kondisi` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_kode_sampel` (`id_kode_sampel`),
  CONSTRAINT `sampel_ibfk_1` FOREIGN KEY (`id_kode_sampel`) REFERENCES `kode_sampel` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sampel`
--

LOCK TABLES `sampel` WRITE;
/*!40000 ALTER TABLE `sampel` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `sampel` VALUES (9,7,1,'122111','Test 1','aa aa','Metro Utara','2019-08-03',1,'Kurang Baik'),(10,8,0,'444','Test 2','axcvbnxcvb','Metro Selatan','2019-08-10',4,'Baik');
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

-- Dump completed on: Sat, 24 Aug 2019 11:51:37 +0200
