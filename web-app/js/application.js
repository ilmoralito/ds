(() => {
    const context = document.querySelector('#chart');
    const queryType = document.querySelector('.nav-pills li.active a').textContent;
    const data = [];

    const dataset = [...document.querySelectorAll('.data')].reduce((accumulator, currentValue) => {
        const object = {};

        object.month = parseInt(currentValue.dataset.month, 10) - 1;
        object.quantity = parseInt(currentValue.dataset.quantity, 10);

        accumulator.push(object);

        return accumulator;
    }, []);

    for (let i = 0; i <= 11; i++) {
        const obj = dataset.find(data => data.month === i);

        data.push(obj ? obj.quantity : 0);
    }

    const chart = new Chart(context, {
        type: 'bar',
        data: {
            labels: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
            datasets: [{
                label: `Resultado ${queryType}`,
                data: data,
                backgroundColor: [
                    'rgba(255, 255, 255, 0)',
                    'rgba(255, 255, 255, 0)',
                    'rgba(255, 255, 255, 0)',
                    'rgba(255, 255, 255, 0)',
                    'rgba(255, 255, 255, 0)',
                    'rgba(255, 255, 255, 0)',
                    'rgba(255, 255, 255, 0)',
                    'rgba(255, 255, 255, 0)',
                    'rgba(255, 255, 255, 0)',
                    'rgba(255, 255, 255, 0)',
                    'rgba(255, 255, 255, 0)',
                    'rgba(255, 255, 255, 0)',
                ],
                borderColor: [
                    'rgba(255,99,132,1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(153, 102, 255, 1)',
                    'rgba(255, 159, 64, 1)',
                    'rgba(255,99,132,1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(153, 102, 255, 1)',
                    'rgba(255, 159, 64, 1)'
                ],
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero:true
                    }
                }]
            }
        }
    });
})();
