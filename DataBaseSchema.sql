--
-- PostgreSQL database dump
--

-- Dumped from database version 17.2 (Debian 17.2-1.pgdg120+1)
-- Dumped by pg_dump version 17.2 (Debian 17.2-1.pgdg120+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: seq_cidade; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.seq_cidade
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: seq_estacao; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.seq_estacao
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: seq_estado; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.seq_estado
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: seq_report_etl; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.seq_report_etl
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: seq_resumo_mensal; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.seq_resumo_mensal
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: seq_rio; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.seq_rio
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: seq_vazao_diaria; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.seq_vazao_diaria
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: tb_cidade; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tb_cidade (
    co_seq_cidade bigint DEFAULT nextval('public.seq_cidade'::regclass) NOT NULL,
    co_estado integer,
    nome character varying(44) NOT NULL
);


--
-- Name: tb_config_etl; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tb_config_etl (
    co_config_etl integer NOT NULL,
    st_ativo integer NOT NULL,
    data_atualizacao_inicial date
);


--
-- Name: COLUMN tb_config_etl.st_ativo; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.tb_config_etl.st_ativo IS 'Indica se o ETL está ativo. 1 - Ativo. 2 - Inativo';


--
-- Name: COLUMN tb_config_etl.data_atualizacao_inicial; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.tb_config_etl.data_atualizacao_inicial IS 'Indica a data base do ETL, deve buscar dados posteriores à essa data';


--
-- Name: tb_estacao; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tb_estacao (
    co_seq_estacao bigint DEFAULT nextval('public.seq_estacao'::regclass) NOT NULL,
    co_rio bigint,
    co_cidade bigint,
    codigo_estacao bigint NOT NULL,
    codigo_bacia bigint,
    codigo_sub_bacia bigint,
    nome character varying(144),
    latitude character varying(44),
    longitude character varying(44),
    altitude character varying(44),
    operando integer,
    ultima_atualizacao timestamp without time zone
);


--
-- Name: COLUMN tb_estacao.operando; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.tb_estacao.operando IS 'Indica se a estação ainda está operando. 1 - Sim, 0 - Não';


--
-- Name: tb_estado; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tb_estado (
    co_seq_estado integer DEFAULT nextval('public.seq_estado'::regclass) NOT NULL,
    nome character varying(44) NOT NULL
);


--
-- Name: tb_report_etl; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tb_report_etl (
    co_seq_report_etl bigint DEFAULT nextval('public.seq_report_etl'::regclass) NOT NULL,
    data_inicio_etl timestamp without time zone NOT NULL,
    data_fim_etl timestamp without time zone NOT NULL,
    ds_erro character varying(128)
);


--
-- Name: COLUMN tb_report_etl.data_inicio_etl; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.tb_report_etl.data_inicio_etl IS 'Data do início de um processo de etl';


--
-- Name: COLUMN tb_report_etl.data_fim_etl; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.tb_report_etl.data_fim_etl IS 'Data do fim de um processo de etl';


--
-- Name: COLUMN tb_report_etl.ds_erro; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.tb_report_etl.ds_erro IS 'Descricao de um possível erro em um processo de etl';


--
-- Name: tb_resumo_mensal; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tb_resumo_mensal (
    co_seq_resumo_mensal bigint DEFAULT nextval('public.seq_resumo_mensal'::regclass) NOT NULL,
    co_estacao bigint NOT NULL,
    data_inicial timestamp without time zone,
    data_insercao_ana timestamp without time zone,
    metodo_obtencao integer,
    nivel_consistencia integer,
    vazao_media double precision,
    vazao_maxima double precision,
    vazao_minima double precision,
    vazao_media_real double precision,
    vazao_maxima_real double precision,
    vazao_minima_real double precision
);


--
-- Name: tb_rio; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tb_rio (
    co_seq_rio bigint DEFAULT nextval('public.seq_rio'::regclass) NOT NULL,
    nome character varying(144) NOT NULL,
    descricao character varying(88)
);


--
-- Name: tb_rio_cidade; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tb_rio_cidade (
    co_rio bigint NOT NULL,
    co_cidade bigint NOT NULL
);


--
-- Name: tb_vazao_diaria; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tb_vazao_diaria (
    co_seq_vazao_diaria bigint DEFAULT nextval('public.seq_vazao_diaria'::regclass) NOT NULL,
    co_resumo_mensal bigint NOT NULL,
    data_vazao date,
    vazao double precision,
    vazao_status integer
);


--
-- Name: tb_cidade pk_cidade; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tb_cidade
    ADD CONSTRAINT pk_cidade PRIMARY KEY (co_seq_cidade);


--
-- Name: tb_config_etl pk_config_etl; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tb_config_etl
    ADD CONSTRAINT pk_config_etl PRIMARY KEY (co_config_etl);


--
-- Name: tb_estacao pk_estacao; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tb_estacao
    ADD CONSTRAINT pk_estacao PRIMARY KEY (co_seq_estacao);


--
-- Name: tb_estado pk_estado; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tb_estado
    ADD CONSTRAINT pk_estado PRIMARY KEY (co_seq_estado);


--
-- Name: tb_report_etl pk_report_etl; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tb_report_etl
    ADD CONSTRAINT pk_report_etl PRIMARY KEY (co_seq_report_etl);


--
-- Name: tb_resumo_mensal pk_resumo_mensal; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tb_resumo_mensal
    ADD CONSTRAINT pk_resumo_mensal PRIMARY KEY (co_seq_resumo_mensal);


--
-- Name: tb_rio pk_rio; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tb_rio
    ADD CONSTRAINT pk_rio PRIMARY KEY (co_seq_rio);


--
-- Name: tb_rio_cidade pk_rio_cidade; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tb_rio_cidade
    ADD CONSTRAINT pk_rio_cidade PRIMARY KEY (co_rio, co_cidade);


--
-- Name: tb_vazao_diaria pk_vazao_diaria; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tb_vazao_diaria
    ADD CONSTRAINT pk_vazao_diaria PRIMARY KEY (co_seq_vazao_diaria);


--
-- Name: in_cidade_estado; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX in_cidade_estado ON public.tb_cidade USING btree (co_estado);


--
-- Name: in_estacao_cidade; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX in_estacao_cidade ON public.tb_estacao USING btree (co_cidade);


--
-- Name: in_estacao_rio; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX in_estacao_rio ON public.tb_estacao USING btree (co_rio);


--
-- Name: in_resumo_mensal_estacao; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX in_resumo_mensal_estacao ON public.tb_resumo_mensal USING btree (co_estacao);


--
-- Name: in_vazao_diaria_resumo_mensal; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX in_vazao_diaria_resumo_mensal ON public.tb_vazao_diaria USING btree (co_resumo_mensal);


--
-- Name: tb_estacao fk_co_cidade; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tb_estacao
    ADD CONSTRAINT fk_co_cidade FOREIGN KEY (co_cidade) REFERENCES public.tb_cidade(co_seq_cidade);


--
-- Name: tb_resumo_mensal fk_co_estacao; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tb_resumo_mensal
    ADD CONSTRAINT fk_co_estacao FOREIGN KEY (co_estacao) REFERENCES public.tb_estacao(co_seq_estacao);


--
-- Name: tb_cidade fk_co_estado; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tb_cidade
    ADD CONSTRAINT fk_co_estado FOREIGN KEY (co_estado) REFERENCES public.tb_estado(co_seq_estado);


--
-- Name: tb_vazao_diaria fk_co_resumo_mensal; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tb_vazao_diaria
    ADD CONSTRAINT fk_co_resumo_mensal FOREIGN KEY (co_resumo_mensal) REFERENCES public.tb_resumo_mensal(co_seq_resumo_mensal);


--
-- Name: tb_estacao fk_co_rio; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tb_estacao
    ADD CONSTRAINT fk_co_rio FOREIGN KEY (co_rio) REFERENCES public.tb_rio(co_seq_rio);


--
-- Name: tb_rio_cidade fk_rc_cidade; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tb_rio_cidade
    ADD CONSTRAINT fk_rc_cidade FOREIGN KEY (co_cidade) REFERENCES public.tb_cidade(co_seq_cidade);


--
-- Name: tb_rio_cidade fk_rc_rio; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tb_rio_cidade
    ADD CONSTRAINT fk_rc_rio FOREIGN KEY (co_rio) REFERENCES public.tb_rio(co_seq_rio);


--
-- PostgreSQL database dump complete
--

