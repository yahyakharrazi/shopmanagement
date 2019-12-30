-- phpMyAdmin SQL Dump
-- version 4.7.7
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le :  lun. 23 déc. 2019 à 16:48
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
(1, 'ya', 'ya', '+21236558', 'aslas@fl.ma', 'lot lot lot a lot of lot'),
(2, 'fertlan', 'fertlan', 'fertlan', 'fertlan', 'fertlan'),
(3, 'alwl', 'alwl', 'alwl', 'alwl', 'alwl');

-- --------------------------------------------------------

--
-- Structure de la table `lignecommande`
--

CREATE TABLE `lignecommande` (
  `id` int(11) NOT NULL,
  `idProduit` int(11) NOT NULL,
  `idVente` int(11) NOT NULL,
  `total` float NOT NULL,
  `quantite` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `paiement`
--

CREATE TABLE `paiement` (
  `id` int(11) NOT NULL,
  `idVente` int(11) NOT NULL,
  `num` int(11) NOT NULL,
  `montant` double NOT NULL,
  `date` text NOT NULL,
  `type` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
(5, 'daguia rafiq not found', 250, 280, 4),
(6, 'aw', 180, 150, 3);

-- --------------------------------------------------------

--
-- Structure de la table `vente`
--

CREATE TABLE `vente` (
  `id` int(11) NOT NULL,
  `date` varchar(10) NOT NULL,
  `idClient` int(11) NOT NULL,
  `total` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `vente`
--

INSERT INTO `vente` (`id`, `date`, `idClient`, `total`) VALUES
(1, '10/10/2018', 1, 155),
(2, '10/10/2018', 1, 0),
(3, '16/01/1988', 1, 0);

--
-- Index pour les tables déchargées
--

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
  ADD PRIMARY KEY (`id`);

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
  ADD KEY `fk_vente_client` (`idClient`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `categorie`
--
ALTER TABLE `categorie`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `client`
--
ALTER TABLE `client`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `lignecommande`
--
ALTER TABLE `lignecommande`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `paiement`
--
ALTER TABLE `paiement`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `produit`
--
ALTER TABLE `produit`
  MODIFY `id` bigint(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT pour la table `vente`
--
ALTER TABLE `vente`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `produit`
--
ALTER TABLE `produit`
  ADD CONSTRAINT `fk_produit_categorie` FOREIGN KEY (`categorie`) REFERENCES `categorie` (`id`);

--
-- Contraintes pour la table `vente`
--
ALTER TABLE `vente`
  ADD CONSTRAINT `fk_vente_client` FOREIGN KEY (`idClient`) REFERENCES `client` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
