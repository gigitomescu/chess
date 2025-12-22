Set-Location -Path 'D:\projects\chess\chess-frontend'
Write-Host 'Running npm install (frontend)...'
npm install
Write-Host 'Starting frontend (ng serve)'
npm start -- --proxy-config proxy.conf.json
