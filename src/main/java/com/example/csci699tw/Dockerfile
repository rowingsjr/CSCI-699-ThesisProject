# Use a Windows Server Core image with .NET Framework
FROM mcr.microsoft.com/dotnet/framework/runtime:4.8-windowsservercore-ltsc2019

# Set the working directory
WORKDIR /app

# Copy the compiled application to the container
COPY ./bin/Release /app

# Set the command to run your application
ENTRYPOINT ["PLCApplication.exe"]