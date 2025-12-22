<#
starts backend and frontend in separate PowerShell windows for development (option 1)

Usage: 
  .\scripts\start-dev.ps1

This script creates two small helper scripts in the `scripts` folder and opens
two PowerShell windows: one for the backend (runs `mvnw.cmd spring-boot:run` with
`PORT=8080`) and one for the frontend (`npm install` then `npm start`).
#>

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$repoRoot = Resolve-Path (Join-Path $scriptDir "..")

$backendScript = Join-Path $scriptDir "start-backend.ps1"
$frontendScript = Join-Path $scriptDir "start-frontend.ps1"

$backendContent = @"
`$env:PORT = '8080'
Set-Location -Path '$repoRoot'
Write-Host 'Starting backend (Spring Boot) in:' (Get-Location)
.\mvnw.cmd spring-boot:run
"@

$frontendContent = @"
Set-Location -Path '$repoRoot\chess-frontend'
Write-Host 'Running npm install (frontend)...'
npm install
Write-Host 'Starting frontend (ng serve)'
npm start -- --proxy-config proxy.conf.json
"@

Set-Content -Path $backendScript -Value $backendContent -Encoding UTF8
Set-Content -Path $frontendScript -Value $frontendContent -Encoding UTF8

Write-Host "Opening two PowerShell windows: backend and frontend..."
Start-Process powershell -ArgumentList '-NoExit','-File',$backendScript
Start-Process powershell -ArgumentList '-NoExit','-File',$frontendScript

Write-Host "Started helper windows. Check them for build and server logs."
