# Coding Standards & Naming Conventions

## Java Backend
- Package: `com.turingguild.tgms`
- Classes: PascalCase (`EventController`)
- Methods: camelCase (`createEvent`)
- Constants: UPPER_SNAKE_CASE (`MAX_LOGIN_ATTEMPTS`)
- DTOs: suffix with `Request`/`Response` (`CreateEventRequest`)
- Repositories: suffix with `Repository`
- Services: suffix with `Service`
- Entities: no suffix, plural table names (`User` → `users`)

## TypeScript Frontend
- Components: PascalCase (`EventList.tsx`)
- Functions/variables: camelCase (`fetchEvents`)
- Types/interfaces: PascalCase, no `I` prefix (`Event`, not `IEvent`)
- Files: PascalCase for components, camelCase for utilities
- API functions: verb-first (`getEvents`, `createEvent`)

## REST API
- Endpoints: kebab-case (`/api/admin/students/{id}/reset-password`)
- JSON: camelCase (`batchNumber`)
- HTTP methods: standard verbs (GET, POST, PUT, DELETE)
- Status codes: 200 (ok), 201 (created), 400 (bad request), 401 (unauthorized), 403 (forbidden), 404 (not found)

## SQL
- Table names: lowercase, plural (`users`, `events`)
- Column names: snake_case (`batch_number`, `created_at`)
- Indexes: `idx_table_column`
- Foreign keys: `fk_table_reference`
