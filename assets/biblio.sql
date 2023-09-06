-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 06, 2023 at 10:39 AM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 8.2.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `biblio`
--

-- --------------------------------------------------------

--
-- Table structure for table `authors`
--

CREATE TABLE `authors` (
  `firstName` varchar(100) NOT NULL,
  `lastName` varchar(100) NOT NULL,
  `id` int(11) NOT NULL,
  `delete_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Triggers `authors`
--
DELIMITER $$
CREATE TRIGGER `softDeleteBook-Author` AFTER UPDATE ON `authors` FOR EACH ROW IF OLD.delete_at IS NULL AND NEW.delete_at IS NOT NULL THEN
        UPDATE books b
        JOIN `books_authors` ba ON b.isbn = ba.book
        SET b.delete_at = CURRENT_TIMESTAMP
        WHERE ba.author = OLD.id
        LIMIT 1;
    ELSEIF OLD.delete_at IS NOT NULL AND NEW.delete_at IS NULL THEN
        UPDATE books b
        JOIN `books_authors` ba ON b.isbn = ba.book
        SET b.delete_at = NULL
        WHERE ba.author = OLD.id
        LIMIT 1;
END IF
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `books`
--

CREATE TABLE `books` (
  `isbn` varchar(11) NOT NULL,
  `quantities` int(11) NOT NULL,
  `pages` int(11) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `edition` text DEFAULT NULL,
  `language` varchar(100) NOT NULL,
  `description` text NOT NULL,
  `delete_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `books_authors`
--

CREATE TABLE `books_authors` (
  `book` varchar(11) NOT NULL,
  `author` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `id` int(11) NOT NULL,
  `category` varchar(100) NOT NULL,
  `description` varchar(300) NOT NULL,
  `delete_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Triggers `categories`
--
DELIMITER $$
CREATE TRIGGER `softDeleteBook-Categorie` AFTER UPDATE ON `categories` FOR EACH ROW IF OLD.delete_at IS NULL AND NEW.delete_at IS NOT NULL THEN
        UPDATE books b
        JOIN `categories_books` cb ON b.isbn = cb.book
        SET b.delete_at = CURRENT_TIMESTAMP
        WHERE cb.category = OLD.id
        LIMIT 1;
    ELSEIF OLD.delete_at IS NOT NULL AND NEW.delete_at IS NULL THEN
        UPDATE books b
        JOIN `categories_books` cb ON b.isbn = cb.book
        SET b.delete_at = NULL
        WHERE cb.category = OLD.id
        LIMIT 1;
    END IF
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `categories_books`
--

CREATE TABLE `categories_books` (
  `book` varchar(11) NOT NULL,
  `category` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `loans`
--

CREATE TABLE `loans` (
  `id` int(11) NOT NULL,
  `book` varchar(11) NOT NULL,
  `user` int(11) NOT NULL,
  `bookReference` int(11) NOT NULL,
  `loanDate` timestamp NOT NULL DEFAULT current_timestamp(),
  `expectedReturnDate` timestamp NULL DEFAULT NULL,
  `returnDate` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Triggers `loans`
--
DELIMITER $$
CREATE TRIGGER `waitinglists-up` AFTER UPDATE ON `loans` FOR EACH ROW BEGIN
    DECLARE wlId INTEGER;
    DECLARE u INTEGER;

    IF OLD.returnDate IS NOT NULL AND NEW.returnDate IS NOT NULL THEN
        -- Recherchez l'ID de l'élément de la waiting list correspondant
        SELECT id INTO wlId FROM `waitinglists` wl WHERE wl.loan IS NULL AND wl.book = OLD.book LIMIT 1;

        IF wlId > 0 THEN
            -- Si un élément de la waiting list est trouvé, recherchez l'utilisateur associé
            SELECT user INTO u FROM `waitinglists` wl WHERE wl.id = wlId;

            -- Insérez un nouvel enregistrement dans la table loans
            INSERT INTO loans (book, user, bookReference) VALUES (OLD.book, u, OLD.bookReference);

            -- Mettez à jour la waiting list en marquant l'élément comme prêté
            UPDATE waitinglists SET loan = LAST_INSERT_ID() WHERE id = wlId;
        END IF;
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `logs`
--

CREATE TABLE `logs` (
  `id` int(11) NOT NULL,
  `user` int(11) NOT NULL,
  `book` varchar(11) NOT NULL,
  `create_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `losts`
--

CREATE TABLE `losts` (
  `book` varchar(11) NOT NULL,
  `loastDate` datetime NOT NULL DEFAULT current_timestamp(),
  `lostCount` int(11) NOT NULL DEFAULT 1,
  `description` varchar(300) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `id` int(11) NOT NULL,
  `role` varchar(100) NOT NULL,
  `delete_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Triggers `roles`
--
DELIMITER $$
CREATE TRIGGER `softDeleteUser-Roles` AFTER UPDATE ON `roles` FOR EACH ROW IF OLD.delete_at IS NULL AND NEW.delete_at IS NOT NULL THEN
        UPDATE users u
        JOIN `users_roles` ur ON u.id = ur.role
        SET u.delete_at = CURRENT_TIMESTAMP
        WHERE ur.role = OLD.id
        LIMIT 1;
    ELSEIF OLD.delete_at IS NOT NULL AND NEW.delete_at IS NULL THEN
        UPDATE users u
        JOIN `users_roles` ur ON u.id = ur.role
        SET u.delete_at = NULL
        WHERE ur.role = OLD.id
        LIMIT 1;
    END IF
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `firstName` varchar(30) NOT NULL,
  `lastName` varchar(30) NOT NULL,
  `email` varchar(50) NOT NULL,
  `phone` varchar(50) NOT NULL,
  `gender` varchar(20) NOT NULL,
  `password` text NOT NULL,
  `delete_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users_roles`
--

CREATE TABLE `users_roles` (
  `user` int(11) NOT NULL,
  `role` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `waitinglists`
--

CREATE TABLE `waitinglists` (
  `id` int(11) NOT NULL,
  `book` varchar(11) NOT NULL,
  `user` int(11) NOT NULL,
  `loan` int(11) NOT NULL,
  `create_at` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `authors`
--
ALTER TABLE `authors`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `books`
--
ALTER TABLE `books`
  ADD PRIMARY KEY (`isbn`);

--
-- Indexes for table `books_authors`
--
ALTER TABLE `books_authors`
  ADD KEY `book` (`book`),
  ADD KEY `books_authors_ibfk_1` (`author`);

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `categories_books`
--
ALTER TABLE `categories_books`
  ADD KEY `book` (`book`),
  ADD KEY `category` (`category`);

--
-- Indexes for table `loans`
--
ALTER TABLE `loans`
  ADD PRIMARY KEY (`id`),
  ADD KEY `loans_ibfk_3` (`book`),
  ADD KEY `user` (`user`);

--
-- Indexes for table `logs`
--
ALTER TABLE `logs`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user` (`user`),
  ADD KEY `book` (`book`);

--
-- Indexes for table `losts`
--
ALTER TABLE `losts`
  ADD KEY `book` (`book`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users_roles`
--
ALTER TABLE `users_roles`
  ADD KEY `user` (`user`),
  ADD KEY `users_roles_ibfk_2` (`role`);

--
-- Indexes for table `waitinglists`
--
ALTER TABLE `waitinglists`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user` (`user`),
  ADD KEY `book` (`book`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `authors`
--
ALTER TABLE `authors`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `loans`
--
ALTER TABLE `loans`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `roles`
--
ALTER TABLE `roles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `books_authors`
--
ALTER TABLE `books_authors`
  ADD CONSTRAINT `books_authors_ibfk_1` FOREIGN KEY (`author`) REFERENCES `authors` (`id`),
  ADD CONSTRAINT `books_authors_ibfk_2` FOREIGN KEY (`book`) REFERENCES `books` (`isbn`);

--
-- Constraints for table `categories_books`
--
ALTER TABLE `categories_books`
  ADD CONSTRAINT `categories_books_ibfk_3` FOREIGN KEY (`book`) REFERENCES `books` (`isbn`),
  ADD CONSTRAINT `categories_books_ibfk_4` FOREIGN KEY (`category`) REFERENCES `categories` (`id`);

--
-- Constraints for table `loans`
--
ALTER TABLE `loans`
  ADD CONSTRAINT `loans_ibfk_3` FOREIGN KEY (`book`) REFERENCES `books` (`isbn`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `loans_ibfk_4` FOREIGN KEY (`user`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints for table `logs`
--
ALTER TABLE `logs`
  ADD CONSTRAINT `logs_ibfk_2` FOREIGN KEY (`user`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `logs_ibfk_3` FOREIGN KEY (`book`) REFERENCES `books` (`isbn`);

--
-- Constraints for table `losts`
--
ALTER TABLE `losts`
  ADD CONSTRAINT `losts_ibfk_1` FOREIGN KEY (`book`) REFERENCES `books` (`isbn`);

--
-- Constraints for table `users_roles`
--
ALTER TABLE `users_roles`
  ADD CONSTRAINT `users_roles_ibfk_1` FOREIGN KEY (`user`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `users_roles_ibfk_2` FOREIGN KEY (`role`) REFERENCES `roles` (`id`) ON DELETE NO ACTION;

--
-- Constraints for table `waitinglists`
--
ALTER TABLE `waitinglists`
  ADD CONSTRAINT `waitinglists_ibfk_2` FOREIGN KEY (`user`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `waitinglists_ibfk_3` FOREIGN KEY (`book`) REFERENCES `books` (`isbn`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
