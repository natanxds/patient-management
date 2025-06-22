-- Grant privileges to admin_user
ALTER DATABASE db OWNER TO admin_user;
GRANT ALL ON SCHEMA public TO admin_user;
GRANT ALL ON ALL TABLES IN SCHEMA public TO admin_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO admin_user;