CREATE SCHEMA IF NOT EXISTS `PokeProjectDB` DEFAULT CHARACTER SET utf8;
USE `PokeProjectDB`;


CREATE TABLE `Pokemon` (
                           `id_pokemon` INT NOT NULL AUTO_INCREMENT,
                           `name` VARCHAR(45) NULL,
                           `height` INT NULL,
                           `weight` INT NULL,
                           `base_experience` INT NULL,
                           PRIMARY KEY (`id_pokemon`)
) ENGINE=InnoDB;

CREATE TABLE `Usuario` (
                           `id_usuario` INT NOT NULL AUTO_INCREMENT,
                           `name` VARCHAR(100) NULL,
                           PRIMARY KEY (`id_usuario`)
) ENGINE=InnoDB;

CREATE TABLE `Access` (
                          `id_access` INT NOT NULL AUTO_INCREMENT,
                          `accessed_at` TIMESTAMP NULL,
                          `client_ip` VARCHAR(100) NULL,
                          `id_pokemon` INT NULL,
                          `id_usuario` INT NULL,
                          PRIMARY KEY (`id_access`),
                          INDEX `idx_access_pokemon` (`id_pokemon`),
                          INDEX `idx_access_usuario` (`id_usuario`),
                          CONSTRAINT `fk_access_pokemon`
                              FOREIGN KEY (`id_pokemon`)
                                  REFERENCES `Pokemon` (`id_pokemon`)
                                  ON DELETE NO ACTION
                                  ON UPDATE NO ACTION,
                          CONSTRAINT `fk_access_usuario`
                              FOREIGN KEY (`id_usuario`)
                                  REFERENCES `Usuario` (`id_usuario`)
                                  ON DELETE NO ACTION
                                  ON UPDATE NO ACTION
) ENGINE=InnoDB;
