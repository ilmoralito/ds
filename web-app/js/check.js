(() => {
    const intervalID = window.setInterval(callback, 120000)
    const listTodayActivitiesURL = window.listTodayActivitiesURL;

    function callback() {
        axios.get(`${listTodayActivitiesURL}`)
            .then(response => {
                updateCount(response.data.length);

                cleanBlocks();

                response.data.forEach(activity => {
                    const blocks = sortBlocks(activity.blocks);
                    const requirements = getRequirements(activity);

                    blocks.forEach((block, index, self) => {
                        const target = getTarget(activity.datashow, block);

                        if (index === 0) {
                            target.innerHTML = `
                                ${!isEmpty(requirements) ? buildAnchor(requirements) : ''}
                                <p>${activity.fullName}</p>
                                <p>${activity.classroom}</p>
                            `;
                        }

                        target.classList.add('hasActivity');
                    });
                });
            })
            .catch(error => {
                console.log(error);
            });
    }

    function sortBlocks(blocks) {
        return blocks.split(',').sort();
    }

    function getTarget(datashow, block) {
        return document.querySelector(`[data-datashow="${datashow}"][data-block="${block}"]`);
    }

    function updateCount(count) {
        document.querySelector('#activity-count').innerHTML = count;
    }

    function cleanBlocks() {
        document.querySelectorAll('td.hasActivity').forEach(td => {
            td.textContent = ''
            td.classList.remove('hasActivity');
        });
    }

    function isEmpty(requirements) {
        return Object.keys(requirements).length === 0;
    }

    function getRequirements(activity) {
        const excludeKeys = ['fullName', 'datashow', 'blocks', 'classroom'];

        const mappedObj = Object.keys(activity).reduce((accumulator, currentValue) => {
            if(!excludeKeys.includes(currentValue) && activity[currentValue]) {
                accumulator[currentValue] = activity[currentValue];
            }

            return accumulator;
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
        anchor.textContent = '+';

        Object.keys(dataset).forEach(data => {
            anchor.setAttribute(data, dataset[data]);
        });

        return anchor.outerHTML;
    }
})();
