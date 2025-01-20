INSERT INTO utilisateur (id, nom, prenom, telephone, mot_de_passe, libelle_adresse, code_postal, ville, latitude, longitude) VALUES
    (
        1,
        'Cluzel',
        'Enzo',
        '0623456789',
        'Enzo_123',
        '50 Avenue de Bordeaux',
        '12000',
        'Rodez',
        44.3602355,
        2.5732901
    );

INSERT INTO utilisateur (id, nom, prenom, telephone, mot_de_passe, libelle_adresse, code_postal, ville, latitude, longitude) VALUES
    (
        2,
        'Baudroït',
        'Leïla',
        '0623454789',
        'Leila_123',
        "2 Rue du Duc d'Arpajon",
        '12150',
        "Sévérac-d'Aveyron",
        44.3223815,
        3.0666963
    );

INSERT INTO utilisateur (id, nom, prenom, telephone, mot_de_passe, libelle_adresse, code_postal, ville, latitude, longitude) VALUES
    (
        3,
        'Brïbach',
        'Ahmed',
        '0623456889',
        'Ahmed_123',
        '1 Av. de Brommat',
        '12600',
        'Mur-de-Barrez',
        44.8433422,
        2.6586814
    );

INSERT INTO utilisateur (id, nom, prenom, telephone, mot_de_passe, libelle_adresse, code_postal, ville, latitude, longitude) VALUES
    (
        4,
        'Nicol',
        'Benjamin',
        '0623459889',
        'Benjamin_123',
        '6 Impasse du Suc',
        '81490',
        'Boissezon',
        43.5775202,
        2.3694482
    );

INSERT INTO itineraire (id, id_utilisateur, nom) VALUES
    (
        1,
        1,
        'Les 7 collines de Rodez'
    );

INSERT INTO itineraire (id, id_utilisateur, nom) VALUES
    (
        2,
        2,
        'La longue route'
    );

INSERT INTO appartient (client_id, itineraire_id, position) VALUES
    (
        1,
        1,
        3
    );

INSERT INTO appartient (client_id, itineraire_id, position) VALUES
    (
        2,
        1,
        2
    );

INSERT INTO appartient (client_id, itineraire_id, position) VALUES
    (
        3,
        1,
        1
    );

INSERT INTO appartient (client_id, itineraire_id, position) VALUES
    (
        4,
        2,
        1
    );

INSERT INTO appartient (client_id, itineraire_id, position) VALUES
    (
        5,
        2,
        2
    );

INSERT INTO appartient (client_id, itineraire_id, position) VALUES
    (
        6,
        2,
        3
    );

INSERT INTO appartient (client_id, itineraire_id, position) VALUES
    (
        7,
        2,
        4
    );