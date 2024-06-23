document.addEventListener('DOMContentLoaded', function () {
    const startChatBtn = document.getElementById('start-chat-btn');
    const chatWindow = document.getElementById('chat-window');
    const messages = document.getElementById('messages');
    const restartChatBtn = document.getElementById('restart-chat-btn');

    const questions = [
        { question: "Які парфуми вас цікавлять?", options: ["Чоловічі", "Жіночі", "Унісекс"] },
        { question: "Які аромати вам подобаються?", options: ["Квіткові", "Цитрусові", "Східні", "Деревні", "Фужерні", "Точно не знаю"] },
        { question: "Ви віддаєте перевагу легким або насиченим ароматам?", options: ["Легкі", "Насичені", "Помірні", "Точно не знаю"] },
        { question: "Який час дня ви зазвичай використовуєте парфуми?", options: ["Ранок", "День", "Вечір", "Точно не знаю"] },
        { question: "Для яких подій ви зазвичай використовуєте парфуми?", options: ["Повсякденне використання", "Спеціальні події", "Робота/офіс", "Відпочинок", "Точно не знаю"] },
        { question: "Який сезон вам підходить найбільше для використання парфумів?", options: ["Літо", "Осінь", "Зима", "Весна", "Точно не знаю"] }
    ];

    let currentQuestion = 0;
    let answers = [];

    startChatBtn.addEventListener('click', () => {
        startChatBtn.style.display = 'none';
        chatWindow.style.display = 'flex';
        startChat();
    });

    restartChatBtn.addEventListener('click', () => {
        restartChat();
    });

    function restartChat() {
        messages.innerHTML = '';
        currentQuestion = 0;
        answers = [];
        startChat();
    }

    function startChat() {
        addTypingMessage('Привіт!', false, () => {
            addTypingMessage('Мене звати Роман і я Ваш бот-консультант, оберімо найкращі варіанти парфумів', false, () => {
                setTimeout(() => {
                    addQuestion(questions[currentQuestion]);
                }, 1500);
            });
        });
    }

    function addMessage(message, fromUser = false) {
        const messageDiv = document.createElement('div');
        messageDiv.classList.add(fromUser ? 'user-message' : 'bot-message');
        messageDiv.textContent = message;
        messages.appendChild(messageDiv);
        messages.scrollTop = messages.scrollHeight;
    }

    function addTypingMessage(message, fromUser = false, callback) {
        const messageDiv = document.createElement('div');
        messageDiv.classList.add(fromUser ? 'user-message' : 'bot-message');
        messages.appendChild(messageDiv);

        let index = 0;
        const interval = setInterval(() => {
            messageDiv.textContent += message.charAt(index);
            index++;
            messages.scrollTop = messages.scrollHeight;
            if (index === message.length) {
                clearInterval(interval);
                if (callback) callback();
            }
        }, 50);
    }

    function addQuestion(questionObj) {
        addTypingMessage(questionObj.question, false, () => {
            addOptions(questionObj.options);
        });
    }

    function addOptions(options) {
        const optionsDiv = document.createElement('div');
        optionsDiv.classList.add('options');
        options.forEach(option => {
            const button = document.createElement('button');
            button.textContent = option;
            button.addEventListener('click', () => handleOption(option, optionsDiv));
            optionsDiv.appendChild(button);
        });
        messages.appendChild(optionsDiv);
    }

    function handleOption(option, optionsDiv) {
        addMessage(option, true);
        answers.push(option);
        optionsDiv.remove(); // Remove the options buttons after selection
        currentQuestion++;
        if (currentQuestion < questions.length) {
            addQuestion(questions[currentQuestion]);
        } else {
            markSelectedFilters(answers);
            setTimeout(clickFilterButton, 1000); // Call the function to click the "Filter" button after a short delay
        }
    }

    function clickFilterButton() {
        const filterButton = document.querySelector('.btn.btn-success');
        if (filterButton) {
            filterButton.click(); // Simulate click on the "Filter" button
        }
    }

    function toggleFilter(element, filterName) {
        const input = element.querySelector('input');
        if (input) {
            input.checked = !input.checked;
        }
        element.classList.toggle('selected');
    }

    function markSelectedFilters(answers) {
        console.log('Збережені дані:', answers); // Для перевірки виведення в консоль

        const filterMapping = {
            'Чоловічі': 'Чоловічі',
            'Жіночі': 'Жіночі',
            'Унісекс': 'Унісекс',
            'Квіткові': 'Квіткові',
            'Цитрусові': 'Цитрусові',
            'Східні': 'Східні',
            'Деревні': 'Деревні',
            'Фужерні': 'Фужерні',
            'Легкі': 'Легкі',
            'Насичені': 'Насичені',
            'Помірні': 'Помірні',
            'Ранок': 'Ранок',
            'День': 'День',
            'Вечір': 'Вечір',
            'Повсякденне використання': 'Повсякденні',
            'Спеціальні події': 'Спеціальні',
            'Робота/офіс': 'Робота',
            'Відпочинок': 'Відпочинок',
            'Літо': 'Літо',
            'Осінь': 'Осінь',
            'Зима': 'Зима',
            'Весна': 'Весна'
        };

        answers.forEach(answer => {
            if (answer !== 'Точно не знаю') {
                const filterDiv = document.querySelector(`.filter-option[data-value="${filterMapping[answer]}"]`);
                if (filterDiv) {
                    filterDiv.classList.add('selected');
                    const input = filterDiv.querySelector('input');
                    if (input) {
                        input.checked = true;
                        // Викликаємо функцію toggleFilter для активації фільтра
                        toggleFilter(filterDiv, input.name);
                    }
                }
            }
        });

        // Додамо повідомлення про автоматичне застосування фільтрів
        addMessage("Ось ваші вибрані фільтри були автоматично застосовані.");

        // Затримка перед кліком на кнопку "Фільтрувати" (потрібна може бути для коректної інтеграції)
        setTimeout(clickFilterButton, 1000); // Затримка 1 секунда, можна змінити за необхідності
    }

    function clickFilterButton() {
        const filterButton = document.querySelector('.btn.btn-success');
        if (filterButton) {
            filterButton.click(); // Симулюємо клік на кнопку "Фільтрувати"
        }
    }

    function toggleFilter(element, filterName) {
        const input = element.querySelector('input');
        if (input) {
            input.checked = !input.checked;
        }
        element.classList.toggle('selected');
    }

});
