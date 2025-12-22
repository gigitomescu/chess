$env:PORT = '8080'
Set-Location -Path 'D:\projects\chess'
Write-Host 'Starting backend (Spring Boot) in:' (Get-Location)
.\mvnw.cmd spring-boot:run
