CREATE TABLE "companies" (
	"id" serial NOT NULL,
	"name" TEXT NOT NULL,
	"authkey" TEXT NOT NULL,
	CONSTRAINT companies_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "projects" (
	"project_id" serial NOT NULL UNIQUE,
	"company_id" bigint NOT NULL UNIQUE,
	"name" bigint NOT NULL,
	CONSTRAINT projects_pk PRIMARY KEY ("project_id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "users" (
	"id" serial NOT NULL,
	"company_id" serial NOT NULL,
	"name" serial NOT NULL,
	CONSTRAINT users_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "user_projects" (
	"user_id" bigint NOT NULL,
	"project_id" bigint NOT NULL,
	CONSTRAINT user_projects_pk PRIMARY KEY ("user_id","project_id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "report" (
	"report_id" serial NOT NULL,
	"report_type" bigint NOT NULL,
	"parse_result" json NOT NULL,
	"location" TEXT NOT NULL,
	"project_id" bigint NOT NULL,
	CONSTRAINT report_pk PRIMARY KEY ("report_id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "report_types" (
	"id" serial NOT NULL,
	"name" TEXT NOT NULL,
	CONSTRAINT report_types_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);


ALTER TABLE "projects" ADD CONSTRAINT "projects_fk0" FOREIGN KEY ("company_id") REFERENCES "companies"("id");

ALTER TABLE "users" ADD CONSTRAINT "users_fk0" FOREIGN KEY ("company_id") REFERENCES "companies"("id");

ALTER TABLE "user_projects" ADD CONSTRAINT "user_projects_fk0" FOREIGN KEY ("user_id") REFERENCES "users"("id");
ALTER TABLE "user_projects" ADD CONSTRAINT "user_projects_fk1" FOREIGN KEY ("project_id") REFERENCES "projects"("project_id");

ALTER TABLE "report" ADD CONSTRAINT "report_fk0" FOREIGN KEY ("report_type") REFERENCES "report_types"("id");
ALTER TABLE "report" ADD CONSTRAINT "report_fk1" FOREIGN KEY ("project_id") REFERENCES "projects"("project_id");
