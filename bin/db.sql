-- mysqldump-php https://github.com/ifsnop/mysqldump-php
--
-- Host: 127.0.0.1	Database: mutasi
-- ------------------------------------------------------
-- Server version 	5.5.5-10.3.16-MariaDB
-- Date: Sun, 18 Aug 2019 07:20:15 +0200

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
  `username` varchar(191) NOT NULL,
  `password` varchar(191) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `admin` VALUES (9,'admin','21232f297a57a5a743894a0e4a801fc3');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `admin` with 1 row(s)
--

--
-- Table structure for table `karyawan`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `karyawan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nama` varchar(50) NOT NULL,
  `nik` varchar(50) NOT NULL,
  `kelamin` varchar(50) NOT NULL,
  `tempat_lahir` varchar(50) NOT NULL,
  `tanggal_lahir` date NOT NULL,
  `agama` varchar(20) NOT NULL,
  `pendidikan` varchar(50) NOT NULL,
  `pekerjaan` varchar(50) NOT NULL,
  `status_kawin` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `karyawan`
--

LOCK TABLES `karyawan` WRITE;
/*!40000 ALTER TABLE `karyawan` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `karyawan` VALUES (2,'Agung Sapto Margono Dh','15753003','Laki-Laki','Nunggalrejo','2019-08-02','Islam','D3 Manajemen Informatika','Programmer','Lajang'),(3,'Ade Irma Rilyani','15753001','Perempuan','Way Halim','2019-08-14','Islam Juga','D3 MI','Gak tau apa','mmmmmmm');
/*!40000 ALTER TABLE `karyawan` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `karyawan` with 2 row(s)
--

--
-- Table structure for table `mutasi`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mutasi` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_karyawan` int(11) NOT NULL,
  `id_tempat_tugas_lama` int(11) NOT NULL,
  `id_tempat_tugas_baru` int(11) NOT NULL,
  `tanggal` date NOT NULL,
  `no_surat` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_karyawan` (`id_karyawan`),
  KEY `id_tempat_tugas` (`id_tempat_tugas_lama`),
  KEY `id_tempat_tugas_baru` (`id_tempat_tugas_baru`),
  CONSTRAINT `mutasi_ibfk_1` FOREIGN KEY (`id_karyawan`) REFERENCES `karyawan` (`id`),
  CONSTRAINT `mutasi_ibfk_2` FOREIGN KEY (`id_tempat_tugas_lama`) REFERENCES `tempat_tugas` (`id`),
  CONSTRAINT `mutasi_ibfk_3` FOREIGN KEY (`id_tempat_tugas_baru`) REFERENCES `tempat_tugas` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mutasi`
--

LOCK TABLES `mutasi` WRITE;
/*!40000 ALTER TABLE `mutasi` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `mutasi` VALUES (1,3,1,2,'2019-08-18','1234'),(2,2,4,3,'2019-08-14','5476');
/*!40000 ALTER TABLE `mutasi` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `mutasi` with 2 row(s)
--

--
-- Table structure for table `tempat_tugas`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tempat_tugas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nama_t_tugas` varchar(50) NOT NULL,
  `gaji` int(11) NOT NULL,
  `t_jabatan` int(11) NOT NULL,
  `t_keluarga` int(11) NOT NULL,
  `t_komunikasi` int(11) NOT NULL,
  `u_kehadiran` int(11) NOT NULL,
  `purna_tugas` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tempat_tugas`
--

LOCK TABLES `tempat_tugas` WRITE;
/*!40000 ALTER TABLE `tempat_tugas` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `tempat_tugas` VALUES (1,'Test 1',1000,2000,3000,4000,5000,6000),(2,'Test 2',1000,2000,3000,4000,5000,6000),(3,'Test 3',1000,2000,3000,4000,5000,6000),(4,'Test 2 3 4',12000,23000,34000,45000,56000,67000);
/*!40000 ALTER TABLE `tempat_tugas` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `tempat_tugas` with 4 row(s)
--

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on: Sun, 18 Aug 2019 07:20:15 +0200
