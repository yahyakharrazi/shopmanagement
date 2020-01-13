-- phpMyAdmin SQL Dump
-- version 4.7.7
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le :  lun. 13 jan. 2020 à 21:41
-- Version du serveur :  10.1.30-MariaDB
-- Version de PHP :  7.2.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `magasinmanagement2`
--

-- --------------------------------------------------------

--
-- Structure de la table `admin`
--

CREATE TABLE `admin` (
  `id` int(11) NOT NULL,
  `username` varchar(55) DEFAULT NULL,
  `password` varchar(55) DEFAULT NULL,
  `firstname` varchar(55) DEFAULT NULL,
  `lastname` varchar(55) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `categorie`
--

CREATE TABLE `categorie` (
  `id` int(11) NOT NULL,
  `designation` varchar(55) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `categorie`
--

INSERT INTO `categorie` (`id`, `designation`) VALUES
(1, 'categorie'),
(2, 'categorie 2'),
(3, 'categorie 3'),
(4, 'categorie 4');

-- --------------------------------------------------------

--
-- Structure de la table `client`
--

CREATE TABLE `client` (
  `id` int(11) NOT NULL,
  `nom` varchar(55) NOT NULL,
  `prenom` varchar(55) NOT NULL,
  `telephone` varchar(20) NOT NULL,
  `email` varchar(50) NOT NULL,
  `adresse` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `client`
--

INSERT INTO `client` (`id`, `nom`, `prenom`, `telephone`, `email`, `adresse`) VALUES
(1, 'Client comptoir', 'Client comptoir', '---', '---', '---'),
(2, 'fertlan', 'fertlan', 'fertlan', 'fertlan', 'fertlan'),
(3, 'alwl', 'alwl', 'alwl', 'alwl', 'alwl'),
(4, 'prenom', 'name', '44444', 'emaail', 'aahahaha'),
(5, 'jsakasj', 'ajkssakjkjsd', 'asjksajkj', 'askjsajakj', 'askaskj'),
(6, 'cccaaa', 'client', '4545445', 'akslask@gmial.css', 'aasas'),
(7, 'sskdlql', 'cliet', '0625417895', 'Qass@fsk;.com', 'lot 5 moghrib');

-- --------------------------------------------------------

--
-- Structure de la table `compte`
--

CREATE TABLE `compte` (
  `nom` varchar(55) DEFAULT NULL,
  `solde` float DEFAULT NULL,
  `code` varchar(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `compte`
--

INSERT INTO `compte` (`nom`, `solde`, `code`) VALUES
('kharrazi', 10000, '111');

-- --------------------------------------------------------

--
-- Structure de la table `lignecommande`
--

CREATE TABLE `lignecommande` (
  `id` int(11) NOT NULL,
  `produit` bigint(11) NOT NULL,
  `vente` int(11) NOT NULL,
  `total` float NOT NULL,
  `quantite` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `lignecommande`
--

INSERT INTO `lignecommande` (`id`, `produit`, `vente`, `total`, `quantite`) VALUES
(1, 1, 1, 15000, 150),
(2, 3, 2, 5625, 45),
(8, 3, 3, 1250, 10),
(9, 4, 5, 1000, 4);

-- --------------------------------------------------------

--
-- Structure de la table `paiement`
--

CREATE TABLE `paiement` (
  `id` int(11) NOT NULL,
  `vente` int(11) NOT NULL,
  `total` float NOT NULL,
  `date` text NOT NULL,
  `reglement` varchar(10) NOT NULL,
  `proprietaire` varchar(55) DEFAULT NULL,
  `typeTraite` varchar(10) DEFAULT NULL,
  `dateEffet` text,
  `datePrevue` text,
  `numCheque` varchar(25) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `paiement`
--

INSERT INTO `paiement` (`id`, `vente`, `total`, `date`, `reglement`, `proprietaire`, `typeTraite`, `dateEffet`, `datePrevue`, `numCheque`) VALUES
(4, 1, 10000, '12-01-2020', 'espece', NULL, NULL, NULL, NULL, NULL),
(5, 1, 1000, '13-01-2020', 'cheque', NULL, NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `produit`
--

CREATE TABLE `produit` (
  `id` bigint(11) NOT NULL,
  `designation` varchar(255) NOT NULL,
  `prixAchat` float NOT NULL,
  `prixVente` float NOT NULL,
  `categorie` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `produit`
--

INSERT INTO `produit` (`id`, `designation`, `prixAchat`, `prixVente`, `categorie`) VALUES
(1, 'produit 11', 100, 120, 1),
(2, 'ghh', 122, 122, 1),
(3, 'asssas', 125, 125, 2),
(4, 'cascettaa rafiq not found', 250, 250, 2),
(5, 'daguia rafiq not found', 250, 280, 4);

-- --------------------------------------------------------

--
-- Structure de la table `vente`
--

CREATE TABLE `vente` (
  `id` int(11) NOT NULL,
  `date` varchar(10) NOT NULL,
  `client` int(11) NOT NULL,
  `total` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `vente`
--

INSERT INTO `vente` (`id`, `date`, `client`, `total`) VALUES
(1, '10/10/2018', 1, 15000),
(2, '10/10/2018', 1, 0),
(3, '16/01/1988', 1, 1250),
(4, '10/12/2015', 1, 0),
(5, '14/12/2011', 2, 1000),
(6, '14/10/2011', 3, 0),
(7, '2019-12-31', 2, 0),
(8, '2020-01-04', 2, 0);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `admin_username_uindex` (`username`);

--
-- Index pour la table `categorie`
--
ALTER TABLE `categorie`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `client`
--
ALTER TABLE `client`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `lignecommande`
--
ALTER TABLE `lignecommande`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_commande_vente` (`vente`),
  ADD KEY `fk_commande_produit` (`produit`);

--
-- Index pour la table `paiement`
--
ALTER TABLE `paiement`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `produit`
--
ALTER TABLE `produit`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_produit_categorie` (`categorie`);

--
-- Index pour la table `vente`
--
ALTER TABLE `vente`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_vente_client` (`client`) USING BTREE;

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `admin`
--
ALTER TABLE `admin`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `categorie`
--
ALTER TABLE `categorie`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `client`
--
ALTER TABLE `client`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT pour la table `lignecommande`
--
ALTER TABLE `lignecommande`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT pour la table `paiement`
--
ALTER TABLE `paiement`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT pour la table `produit`
--
ALTER TABLE `produit`
  MODIFY `id` bigint(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT pour la table `vente`
--
ALTER TABLE `vente`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `lignecommande`
--
ALTER TABLE `lignecommande`
  ADD CONSTRAINT `fk_commande_produit` FOREIGN KEY (`produit`) REFERENCES `produit` (`id`),
  ADD CONSTRAINT `fk_commande_vente` FOREIGN KEY (`vente`) REFERENCES `vente` (`id`);

--
-- Contraintes pour la table `produit`
--
ALTER TABLE `produit`
  ADD CONSTRAINT `fk_produit_categorie` FOREIGN KEY (`categorie`) REFERENCES `categorie` (`id`);

--
-- Contraintes pour la table `vente`
--
ALTER TABLE `vente`
  ADD CONSTRAINT `fk_vente_client` FOREIGN KEY (`client`) REFERENCES `client` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
