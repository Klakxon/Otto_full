// app.js
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
      getRecommendedFragrances(answers);
    }
  }

  function getRecommendedFragrances(answers) {
    console.log('Збережені дані:', answers); // Для перевірки виведення в консоль

    // Відправлення запиту на сервер для отримання відфільтрованих парфумів
    fetch('/api/recommendations', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ answers: answers })
    })
      .then(response => response.json())
      .then(data => {
        addMessage("Ось список ароматів, які можуть вам підійти:");
        data.forEach(fragrance => {
          addMessage(fragrance.name);
        });
      })
      .catch(error => {
        console.error('Error:', error);
        addMessage("Сталася помилка. Спробуйте ще раз пізніше.");
      });
  }
});
