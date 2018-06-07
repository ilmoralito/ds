const app = {};

app.model = {
    dataset: Array.from(document.querySelectorAll('[data-applicant]')).map(data => Object.assign({}, data.dataset)),

    filtered: []
};

app.controller = {
    init() {
        app.view.init();
    },

    filter(applicant) {
       const result = app.model.dataset.filter(data => data.applicant.toLowerCase().includes(applicant.toLowerCase()));

       app.model.filtered = result;
    },

    getFiltered() {
        return app.model.filtered;
    }
};

app.view = {
    init() {
        const filterBox = document.querySelector('#filter-box');
        this.filterList = document.querySelector('#filter-list');

        filterBox.addEventListener('keyup', event => {
            app.controller.filter(event.target.value);

            this.render();
        });

    },

    render() {
        const dataset = app.controller.getFiltered();
        const template = dataset.map(data => {
            return `
                <p class="applicant">${data.applicant}</p>

                ${this.getBody(data.requests)}`;
        }).join('');

        this.filterList.innerHTML = template;
    },

    getBody(requests) {
        const dataset = JSON.parse(requests);

        return dataset.map(data => {
            return `
                <div class="bar" style="width: ${data.count + 20}px;">${data.count}</div>
                <div class="school">${data.school}</div>`;
        }).join('');
    }
};

app.controller.init();
