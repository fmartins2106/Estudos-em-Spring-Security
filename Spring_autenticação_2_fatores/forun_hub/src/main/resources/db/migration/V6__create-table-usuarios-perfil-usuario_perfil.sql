CREATE TABLE usuarios(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL,
    nome_completo VARCHAR(100) NOT NULL,
    nome_usuario VARCHAR(100) NOT NULL UNIQUE,
    mini_biografia VARCHAR(30),
    biografia TEXT,
    verificado boolean NOT NULL DEFAULT 1,
    token VARCHAR(64),
    expiracao_token TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT 1
);


CREATE TABLE perfis(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE usuarios_perfis(
    usuario_id BIGINT NOT NULL,
    perfil_id BIGINT NOT NULL,
    PRIMARY KEY(usuario_id, perfil_id),
    CONSTRAINT USUARIOS_PERFIS_FK_USUARIO FOREIGN KEY(usuario_id) REFERENCES usuarios(id),
    CONSTRAINT USUARIOS_PERFIS_FK_PERFIL FOREIGN KEY(perfil_id) REFERENCES perfis(id)

);

INSERT INTO perfis(nome) VALUES('ESTUDANTE');
INSERT INTO perfis(nome) VALUES('INSTRUTOR');
INSERT INTO perfis(nome) VALUES('MODERADOR');
INSERT INTO perfis(nome) VALUES('ADMIN');