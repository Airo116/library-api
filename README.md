# 📚 Library API — Веб-приложение для управления библиотекой

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-brightgreen)
![SQLite](https://img.shields.io/badge/SQLite-3-blue)
![GitHub last commit](https://img.shields.io/github/last-commit/Airo116/library-api)

## 📖 О проекте

Полноценное веб-приложение для управления библиотекой с возможностью:
- 📋 Просматривать все книги
- ➕ Добавлять новые книги
- ✏️ Редактировать существующие
- 🗑️ Удалять книги
- 🔍 Искать по автору и названию

## 🛠️ Технологии

- **Backend**: Java 17, Spring Boot 4.0.3
- **База данных**: SQLite, Hibernate (JPA)
- **Frontend**: HTML5, CSS3, JavaScript (Fetch API)
- **Инструменты**: Maven, Git, GitHub

## 🚀 Как запустить

### 1. Клонировать репозиторий
```bash
git clone https://github.com/Airo116/library-api.git
cd library-api

##📸 Скриншоты

![Главная страница](screenshots/main.png)
![Список](screenshots/list.png)

🌟 Возможности
📋 CRUD операции
GET /api/books — получить все книги

GET /api/books/{id} — получить книгу по ID

POST /api/books — добавить книгу

PUT /api/books/{id} — обновить книгу

DELETE /api/books/{id} — удалить книгу

GET /api/books/search?author=&title= — поиск книг

🎨 Интерфейс
Современный адаптивный дизайн

Форма добавления с валидацией

Редактирование при клике на книгу

Поиск в реальном времени

Статистика (книг, авторов, сред. страниц)

🔧 Требования
Java 17 или выше

Maven 3.6+

Браузер с поддержкой JavaScript

👨‍💻 Автор
Халяпов Айрат

GitHub: @Airo116

📄 Лицензия
Проект создан в учебных целях. Свободно для использования и модификации.

⭐ Если понравился проект — поставь звезду на GitHub!