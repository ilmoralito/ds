(() => {
    const context = document.querySelector('#chart');
    const label = document.querySelector('.nav-pills li.active a').textContent;
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

    Chart.plugins.register({
        beforeDraw: function(chartInstance) {
            const ctx = chartInstance.chart.ctx;
            ctx.fillStyle = "white";
            ctx.fillRect(0, 0, chartInstance.chart.width, chartInstance.chart.height);
        }
    });

    const chart = new Chart(context, {
        type: 'bar',
        data: {
            labels: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
            datasets: [{
                label: `Resultado ${label}`,
                data: data,
                backgroundColor: [
                    'rgba(113, 240, 50, 1)',
                    'rgba(179, 59, 228, 1)',
                    'rgba(240, 34, 82, 1)',
                    'rgba(15, 218, 60, 1)',
                    'rgba(236, 160, 230, 1)',
                    'rgba(95, 207, 217, 1)',
                    'rgba(107, 166, 231, 1)',
                    'rgba(239, 232, 243, 1)',
                    'rgba(241, 0, 42, 1)',
                    'rgba(132, 48, 132, 1)',
                    'rgba(1, 195, 111, 1)',
                    'rgba(123, 200, 34, 1)'
                ]
            }]
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
            }
        }
    });
})();
