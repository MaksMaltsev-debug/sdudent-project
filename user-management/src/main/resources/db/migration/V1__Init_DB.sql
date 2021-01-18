CREATE TABLE `users` (
  `id` varchar(255) NOT NULL,
  `last_name` varchar(100) DEFAULT NULL,
  `login` varchar(100) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1
