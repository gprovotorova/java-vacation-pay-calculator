# Приложение "Калькулятор отпускных"

Тестовое задание для учебного центра Neoflex

____
### Техническое задание
Приложение "Калькулятор отпускных". Микросервис на SpringBoot + Java 11 c одним API: GET "/calculacte"

Минимальные требования: Приложение принимает твою среднюю зарплату за 12 месяцев и количество дней отпуска - отвечает 
суммой отпускных, которые придут сотруднику. Доп. задание: При запросе также можно указать точные дни ухода в отпуск, 
тогда должен проводиться рассчет отпускных с учётом праздников и выходных.

Проверяться будет чистота кода, структура проекта, название полей\классов, правильность использования паттернов. 
Желательно написание юнит-тестов, проверяющих расчет.
____
Технологический стек:
* Java 11
* Spring Boot
* Lombok
* Maven
* Hibernate
* Mockito
* JUnit
____

CalculatorController:
* GET /calculate - получение списка вещей, добавленных текущим пользователем.

____
### Установка и запуск проекта:
1. Необходимо установить и запустить Docker Desktop (скачать и установить можно с официального сайта https://www.docker.com/products/docker-desktop/)

2. Клонируйте репозиторий проекта на свою локальную машину

    git clone https://github.com/gprovotorova/java-vacation-pay-calculator.git

3. Зайдите в папку проекта в командой строке

4. Соберите проект

    mvn clean package

5. Собрать docker image

    docker build -t java-vacation-pay-calculator .

6. Запустить docker container 

    docker run -p 8080:8080 java-vacation-pay-calculator

7. Остановить docker container

    docker stop java-vacation-pay-calculator

____
Работу можно проверить через postman, используя файл "Vacation Pay Calculator.postman_collection.json"