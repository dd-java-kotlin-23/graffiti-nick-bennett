CREATE TABLE "user" (
  "user_id" bigint PRIMARY KEY,
  "external_key" uuid UNIQUE NOT NULL DEFAULT (uuidv7()),
  "oauth_key" varchar(30) UNIQUE NOT NULL,
  "display_name" nvarchar(50) UNIQUE NOT NULL,
  "created" timestamp NOT NULL
);

CREATE TABLE "canvas" (
  "canvas_id" bigint PRIMARY KEY,
  "external_key" uuid UNIQUE NOT NULL DEFAULT (uuidv7()),
  "owner_id" bigint NOT NULL,
  "width" integer NOT NULL,
  "height" integer NOT NULL,
  "background_color" integer NOT NULL DEFAULT -1,
  "created" timestamp NOT NULL
);

CREATE TABLE "brush_stroke" (
  "brush_stroke_id" bigint PRIMARY KEY,
  "canvas_id" bigint NOT NULL,
  "contributor_id" bigint NOT NULL,
  "color" integer NOT NULL,
  "width" integer NOT NULL,
  "added" timestamp NOT NULL
);

COMMENT ON TABLE "user" IS 'User registry, w/o authentication (handled by OAuth provider';

COMMENT ON COLUMN "user"."external_key" IS 'Generated automatically';

COMMENT ON COLUMN "canvas"."external_key" IS 'Generated automatically';

ALTER TABLE "canvas" ADD FOREIGN KEY ("owner_id") REFERENCES "user" ("user_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "brush_stroke" ADD FOREIGN KEY ("canvas_id") REFERENCES "canvas" ("canvas_id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "brush_stroke" ADD FOREIGN KEY ("contributor_id") REFERENCES "user" ("user_id") DEFERRABLE INITIALLY IMMEDIATE;
