CREATE TABLE `classes` (
  `id` varchar(255) NOT NULL,
  `description` varchar(400) DEFAULT NULL,
  `finish_time` datetime DEFAULT NULL,
  `image` longblob DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `status` varchar(15) DEFAULT NULL,
  `creator_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcotfslo5c1wmx85rudo8mbotl` (`creator_id`),
  CONSTRAINT `FKcotfslo5c1wmx85rudo8mbotl` FOREIGN KEY (`creator_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1

CREATE TABLE `classes_users` (
  `class_id` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  KEY `FKj7by1ak3325bf5wu4ytuvcvc2` (`user_id`),
  KEY `FK78552r6a2okslsxqusgio6u1j` (`class_id`),
  CONSTRAINT `FK78552r6a2okslsxqusgio6u1j` FOREIGN KEY (`class_id`) REFERENCES `classes` (`id`),
  CONSTRAINT `FKj7by1ak3325bf5wu4ytuvcvc2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1

CREATE TABLE `image` (
  `user_id` varchar(255) NOT NULL,
  `image` longblob DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1

CREATE TABLE `users` (
  `id` varchar(255) NOT NULL,
  `activity_in_classes` bit(1) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `first_name` varchar(100) DEFAULT NULL,
  `information_about_updates` bit(1) DEFAULT NULL,
  `last_name` varchar(100) DEFAULT NULL,
  `notification_class_start` bit(1) DEFAULT NULL,
  `status` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1