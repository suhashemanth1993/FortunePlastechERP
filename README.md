# FortunePlastechERP


## Two-Factor Authentication (2FA) with TOTP

This ERP system supports secure user authentication with optional Two-Factor Authentication (2FA) using Time-based One-Time Passwords (TOTP, e.g., Google Authenticator, Authy).

### Backend 2FA Endpoints

**1. Setup 2FA**
```
POST /api/auth/2fa/setup
Body: { "identifier": "<username|email|phone>" }
Response: { "otpauthUrl": "otpauth://...", "qrCodeImage": "<base64>" }
```
*Returns a TOTP secret and QR code for the user to scan with an authenticator app.*

**2. Verify 2FA Code (Enable 2FA)**
```
POST /api/auth/2fa/verify
Body: { "identifier": "<username|email|phone>", "code": "123456" }
Response: { "success": true }
```
*Verifies the TOTP code and enables 2FA for the user.*

**3. Disable 2FA**
```
POST /api/auth/2fa/disable
Body: { "identifier": "<username|email|phone>", "code": "123456" }
Response: { "success": true }
```
*Disables 2FA after verifying the TOTP code.*

**4. Login with 2FA**
```
POST /api/auth/login
Body: { "identifier": "<username|email|phone>", "password": "...", "code": "123456" (if 2FA enabled) }
Response: { "token": "<JWT>" }
```
*If 2FA is enabled, the user must provide a valid TOTP code to log in.*

### Frontend 2FA Usage

- **2FA Setup:**
	- After registration or from the profile/settings page, users can enable 2FA.
	- The UI displays a QR code to scan with an authenticator app (Google Authenticator, Authy, etc.).
	- User enters a 6-digit code from their app to verify and enable 2FA.
- **Login with 2FA:**
	- If 2FA is enabled, after entering username/email/phone and password, the user is prompted for a 6-digit code from their authenticator app.
	- On success, a JWT token is issued for authenticated access.

### Setup & Configuration

**Backend:**
- Java 17, Spring Boot 3, aerogear-otp-java for TOTP.
- User entity supports username, email, or phone as login identifier.
- JWT-based authentication.

**Frontend:**
- React with Material-UI.
- 2FA setup and login flows are integrated in the UI.

**To enable 2FA for a user:**
1. Call `/api/auth/2fa/setup` to get the QR code and secret.
2. Scan the QR code with an authenticator app.
3. Call `/api/auth/2fa/verify` with a valid code from the app.
4. On next login, provide the TOTP code along with credentials.

**To disable 2FA:**
1. Call `/api/auth/2fa/disable` with a valid TOTP code.

---
For more details, see the backend controller and service code, or contact the project maintainer.
