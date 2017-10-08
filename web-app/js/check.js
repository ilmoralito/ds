(() => {
    const intervalID = window.setInterval(callback, 5000)
    const listTodayActivitiesURL = window.listTodayActivitiesURL;

    function callback() {
        const activityTable = document.querySelector('#activity-table');

        axios.get(`${listTodayActivitiesURL}`)
            .then(response => {
                response.data.forEach(activity => {
                    const datashow = activity.datashow;
                    const requirements = getRequirements(activity);
                    const blocks = activity.blocks.split(',').map(block => parseInt(block, 10)).sort();

                    blocks.forEach((block, index, self) => {
                        const target = activityTable.querySelector(`[data-datashow="${datashow}"][data-block="${block}"]`);

                        if (!target.classList.contains('hasActivity')) {
                            if (index === 0) {
                                target.innerHTML = `
                                    ${requirements ? buildAnchor(requirements) : ''}
                                    <p>${activity.fullName}</p>
                                    <p>${activity.classroom}</p>
                                `;
                            }

                            target.classList.add('hasActivity');
                        }
                    });
                });
            })
            .catch(error => {
                console.log(error);
            });
    }

    function getRequirements(activity) {
        const excludeKeys = ['fullName', 'datashow', 'blocks', 'classroom'];

        const mappedObj = Object.keys(activity).reduce((o, item) => {
            if(!excludeKeys.includes(item) && activity[item]) {
                o[item] = activity[item];
            }

            return o;
        }, {});

        return mappedObj;
    }

    function getDataset(requirement) {
        const dataset = Object.keys(requirement).reduce((accumulator, currentValue) => {
            accumulator[`data-${currentValue}`] = requirement[currentValue];

            return accumulator;
        }, {});

        return dataset;
    }

    function buildAnchor(requirements) {
        const dataset = getDataset(requirements);
        const anchor = document.createElement('a');

        anchor.setAttribute('class', 'show-modal');
        anchor.setAttribute('href', '#');

        Object.keys(dataset).forEach(data => {
            anchor.setAttribute(data, dataset[data]);
        });

        anchor.textContent = '+';

        const anchorHTML = anchor.outerHTML

        return anchorHTML;
    }
})();
