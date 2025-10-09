
ALTER TABLE topicos ADD COLUMN autor_id BIGINT ;
ALTER TABLE topicos ADD CONSTRAINT fk_top_autor FOREIGN KEY (autor_id) REFERENCES usuarios(id);

ALTER TABLE respostas ADD COLUMN id_autor BIGINT ;
ALTER TABLE respostas ADD CONSTRAINT fk_resp_autor FOREIGN KEY (id_autor) REFERENCES usuarios(id);
