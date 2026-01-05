# ws_wa_electronic_invoicing

API REST para facturación electrónica SUNAT (Perú) con generación UBL 2.1, firma digital, ZIP y envío por SOAP.

## Requisitos

- Java 21
- Maven 3.9+
- MySQL 8+

## Configuración

Los perfiles disponibles son `dev`, `beta` y `prod` en `src/main/resources/application.yml`.

### Datos de conexión

Configura el datasource para cada perfil:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ei_dev
    username: root
    password: root
```

### Certificado digital (P12)

Coloca el certificado en la ruta indicada:

```yaml
app:
  keystore:
    path: ./certs/certificate.p12
    password: changeit
```

### Credenciales SOL

En la tabla `ei_issuer` guarda `usuario_sol` y `clave_sol`. No se registran en logs.

### Endpoints SUNAT

```yaml
app:
  sunat:
    beta-endpoint: https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService
    prod-endpoint: https://e-factura.sunat.gob.pe/ol-ti-itcpfegem/billService
```

### Almacenamiento de archivos

```yaml
app:
  storage:
    base-path: ./storage
    xml-path: xml
    zip-path: zip
    cdr-path: cdr
```

## Flujo de emisión

1. Registrar emisor en `ei_issuer`.
2. Enviar request al endpoint correspondiente.
3. Se persiste en `ei_document` como `DRAFT`.
4. Se genera XML UBL, firma, ZIP.
5. Se envía a SUNAT o se queda en modo `generateOnly`.
6. Se guarda CDR o ticket.

## Estados

- `DRAFT`: documento creado.
- `SIGNED`: XML firmado.
- `ZIPPED`: ZIP generado.
- `SENT`: enviado.
- `ACCEPTED`: aceptado por SUNAT.
- `REJECTED`: rechazado.
- `OBSERVED`: observado.
- `PENDING_TICKET`: pendiente de ticket.
- `VOIDED`: comunicado de baja.

## Ejecución

```bash
mvn clean package
mvn spring-boot:run
```

Swagger:

```
http://localhost:8080/api/v1/swagger-ui.html
```

## Modo mock (dev)

Por defecto `app.sunat.mock-enabled=true` en `dev`:

- Genera XML + firma + ZIP.
- Simula envío y devuelve CDR/ticket fake.

## Troubleshooting

- **Certificado inválido**: Verificar ruta y password del P12.
- **Error SOAP**: Revisar endpoint y credenciales SOL.
- **Errores de BD**: Validar migraciones Flyway y permisos de MySQL.

