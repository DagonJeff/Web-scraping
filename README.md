# Web Scraping

Este repositório contém um projeto de web scraping escrito em Java. O objetivo deste projeto é extrair dados de páginas web automaticamente.

## Funcionalidades

- Extração de dados de páginas web.
- Processamento e armazenamento dos dados extraídos.
- Suporte para extração de dados em formato tabular utilizando Tabula.

## Instalação

### Requisitos

- Java JDK 8 ou superior
- [Tabula](https://tabula.technology/)

### Passos para instalação

1. Clone o repositório:

   ```sh
   git clone https://github.com/DagonJeff/Web-scraping.git
   cd Web-scraping
   ```

2. Compile o projeto:

   ```sh
   javac -d bin src/*.java
   ```

3. Execute o projeto:

   ```sh
   java -cp bin Main
   ```

### Instalação do Tabula

Para utilizar o Tabula, siga os passos abaixo:

1. Baixe o Tabula no [site oficial](https://tabula.technology/).
2. Extraia o conteúdo do arquivo baixado.
3. Adicione o caminho do executável do Tabula ao seu PATH do sistema.

### Passos para Configuração Cloud

1. Configure suas credenciais AWS:

   ```sh
   aws configure
   ```

2. Inicialize o Terraform:

   ```sh
   terraform init
   ```

3. Aplique a configuração do Terraform:

   ```sh
   terraform apply
   ```

4. Configure o GitHub Actions para CI/CD, adicionando as credenciais AWS como segredos no GitHub.

### Arquivo main.tf

```hcl
provider "aws" {
  region = "us-west-2"
}

resource "aws_instance" "web" {
  ami           = "ami-0c55b159cbfafe1f0"
  instance_type = "t2.micro"

  tags = {
    Name = "WebScrapingInstance"
  }
}

resource "aws_s3_bucket" "bucket" {
  bucket = "web-scraping-data"
  acl    = "private"
}
```

### Arquivo deploy.yml

```yaml
name: Deploy to AWS

on:
  push:
    branches:
      - main

jobs:
  deploy:
    runs-on: ubuntu-latest

    steps:
    - name: Checkout code
      uses: actions/checkout@v2
    
    - name: Set up JDK 11
      uses: actions/setup-java@v2
      with:
        java-version: '11'
    
    - name: Build with Maven
      run: mvn clean install
    
    - name: Configure AWS credentials
      uses: aws-actions/configure-aws-credentials@v1
      with:
        aws-access-key-id: ${{ secrets.AWS_ACCESS_KEY_ID }}
        aws-secret-access-key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
        aws-region: us-west-2
    
    - name: Deploy to EC2
      run: |
        # Comandos para implantar o aplicativo no EC2
```

**Autor:** [DagonJeff](https://github.com/DagonJeff)