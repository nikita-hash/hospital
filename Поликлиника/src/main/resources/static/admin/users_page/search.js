var  doctorCard = document.getElementById('list_doctor')

const doctorCards = Array.from(doctorCard.querySelectorAll('.card'));

// Функция для обработки ввода в инпуте поиска
function handleSearch() {
    // Получаем значение из инпута поиска
    const searchValue = document.getElementById('searchInput').value.toLowerCase();

    // Перебираем все карточки врачей
    doctorCards.forEach((card) => {
        // Получаем все поля текста в карточке врача
        const cardTexts = Array.from(card.querySelectorAll('.card-title, .card-body'));

        // Проверяем, соответствует ли хотя бы одно поле текста поисковому значению
        const isMatching = cardTexts.some((textElement) => {
            return textElement.textContent.toLowerCase().includes(searchValue);
        });

        // Отображаем или скрываем карточку врача, в зависимости от результата поиска
        card.style.display = isMatching ? 'block' : 'none';
    });
}

// Привязываем функцию handleSearch к событию input для инпута поиска
document.getElementById('searchInput').addEventListener('input', handleSearch);