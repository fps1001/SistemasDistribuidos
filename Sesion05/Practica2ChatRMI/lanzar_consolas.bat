@echo off

REM Ejecuta el primer archivo batch en una nueva ventana de PowerShell.
start "Registro" powershell -Command "& 'G:\Mi unidad\DISTRIBUIDOS\repo\Sesion05\Practica2ChatRMI\registro.bat'"

REM Espera un segundo.
timeout /t 1 /nobreak >nul

REM Ejecuta el segundo archivo batch en una nueva ventana de PowerShell.
start "Servidor" powershell -Command "& 'G:\Mi unidad\DISTRIBUIDOS\repo\Sesion05\Practica2ChatRMI\servidor.bat'"

REM Espera un segundo.
timeout /t 1 /nobreak >nul

REM Ejecuta el tercer archivo batch en una nueva ventana de PowerShell.
start "Cliente" powershell -Command "& 'G:\Mi unidad\DISTRIBUIDOS\repo\Sesion05\Practica2ChatRMI\cliente.bat'"

REM Espera un segundo.
timeout /t 1 /nobreak >nul

echo Todos los archivos batch se han ejecutado en sus respectivas consolas de PowerShell.


