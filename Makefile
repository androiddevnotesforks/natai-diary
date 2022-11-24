check:
	php --version && node --version && npm --version && npx --version && symfony -V && docker --version && docker compose version

install:
	cd api && docker compose up -d && composer install && bin/console doctrine:migr:migr && cd ../frontend && npm install && make frontend-schema

up:
	make api-up & make frontend-up

api-up:
	cd api && docker compose up -d && symfony serve

frontend-up:
	cd frontend && npm run dev

test:
	cd api && bin/phpunit

tunnel:
	ngrok http 8000

frontend-schema:
	cd frontend && npx openapi-codegen gen api