import {Bar} from 'vue-chartjs'
import _ from 'lodash'

let dadosDoGrafico
let router
let filtros
const ELEMENT_TOOLTIP = 'chartjs-tooltip'

export default {
    extends: Bar,
    props: {
        dados: {
            required: true,
            type: Array,
            default: () => {
                return []
            }
        }
    },
    data: function () {
        return {
            intangivelClicado: null,
            datacollection: {
                labels: this.buscarLabels(),
                datasets: [
                    {
                        backgroundColor: '#487b9c',
                        hoverBackgroundColor: '#3A9142',
                        data: this.buscarData()
                    }
                ]
            },
            options: {
                scales: {
                    yAxes: [{
                        ticks: {
                            beginAtZero: true
                        },
                        gridLines: {
                            display: true
                        }
                    }],
                    xAxes: [{
                        ticks: {
                            beginAtZero: true
                        },
                        gridLines: {
                            display: false
                        }
                    }]
                },
                legend: {
                    display: false
                },
                tooltips: {
                    enabled: false,
                    custom: function (tooltipModel) {
                        let tooltipEl = document.getElementById(ELEMENT_TOOLTIP)

                        if (!tooltipEl) {
                            tooltipEl = document.createElement('div')
                            tooltipEl.id = ELEMENT_TOOLTIP
                            tooltipEl.innerHTML = '<table></table>'
                            document.body.appendChild(tooltipEl)
                        }

                        if (tooltipModel.opacity === 0) {
                            tooltipEl.style.opacity = 0
                            return
                        }

                        tooltipEl.classList.remove('above', 'below', 'no-transform')
                        if (tooltipModel.yAlign) {
                            tooltipEl.classList.add(tooltipModel.yAlign)
                        } else {
                            tooltipEl.classList.add('no-transform')
                        }

                        function getBody(bodyItem) {
                            return bodyItem.lines
                        }

                        if (tooltipModel.body) {
                            let titleLines = tooltipModel.title || []
                            let bodyLines = tooltipModel.body.map(getBody)

                            let innerHtml = '<thead>'

                            titleLines.forEach(function (title) {
                                innerHtml += '<tr><th>' + title + '</th></tr>'
                            })
                            innerHtml += '</thead><tbody>'

                            bodyLines.forEach(function (body) {
                                let tipos = formatarTipoIntangiveisParaTooltip(tooltipModel.title[0])
                                let span = '<span></span>'
                                innerHtml += '<tr>' + '<td>' + span + 'Total Intangíveis: ' + body + '</td>' + '</tr>' + tipos
                            })
                            innerHtml += '</tbody>'
                            let tableRoot = tooltipEl.querySelector('table')
                            tableRoot.innerHTML = innerHtml
                        }
                        let position = this._chart.canvas.getBoundingClientRect()

                        tooltipEl.style.opacity = 1
                        tooltipEl.style.position = 'absolute'
                        tooltipEl.style.backgroundColor = '#333333'
                        tooltipEl.style.color = '#fff'
                        tooltipEl.style.borderRadius = '6px'
                        tooltipEl.style.left = position.left + window.pageXOffset + tooltipModel.caretX - 60 + 'px'
                        tooltipEl.style.top = position.top + window.pageYOffset + tooltipModel.caretY - position.top / 4 + 'px'
                        tooltipEl.style.fontFamily = 'Roboto, sans-serif'
                        tooltipEl.style.fontSize = '12px'
                        tooltipEl.style.fontStyle = tooltipModel._bodyFontStyle
                        tooltipEl.style.padding = tooltipModel.yPadding + 'px ' + tooltipModel.xPadding + 'px'
                        tooltipEl.style.pointerEvents = 'none'
                    }
                },
                onClick: function (mouseEvent, chartElement) {
                    let elemento = chartElement[0]
                    let sigla = this.data.labels[elemento._index]
                    let tooltipEl = document.getElementById(ELEMENT_TOOLTIP)
                    tooltipEl.style.opacity = 0
                    filtros.conteudo.value = sigla
                    router.push({name: 'TodosPatrimonios'})
                },
                onHover: (mouseEvent, chartElement) => {
                    mouseEvent.target.style.cursor = chartElement[0] ? 'pointer' : 'default'
                },
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    datalabels: {
                        color: '#487b9c',
                        textAlign: 'center',
                        font: {
                            weight: 'bold',
                            size: 12
                        },
                        display: false
                    }
                }
            }
        }
    },
    mounted() {
        this.renderChart(this.datacollection, this.options)
        this.setarDadosDoGrafico()
    },
    methods: {
        buscarLabels() {
            let labels = []
            for (let item of this.dados) {
                labels.push(item.sigla)
            }
            return labels.sort((a, b) => a > b ? 1 : -1)
        },
        buscarData() {
            let data = []
            let dadosOrdenado = this.dados.sort((a, b) => a.sigla > b.sigla ? 1 : -1)
            for (let item of dadosOrdenado) {
                data.push(item.total)
            }
            return data
        },
        setarDadosDoGrafico() {
            filtros = this.$store.state.patrimonio.todosPatrimonios.filtros
            router = this.$router
            dadosDoGrafico = _.cloneDeep(this.dados)
        }
    }
}

function formatarTipoIntangiveisParaTooltip(sigla) {
    let dado = dadosDoGrafico.filter(dado => dado.sigla === sigla)
    let itemsDoIntangivel = ''
    for (let tipo of dado[0].tipos) {
        itemsDoIntangivel += '<tr><td><span>' + formatarString(tipo.nome) + ': ' + tipo.quantidade + '</span></td></tr>'
    }
    return itemsDoIntangivel
}

function formatarString(tipo) {
    tipo = tipo.toLowerCase()
    tipo = tipo.substring(0, 1).toUpperCase().concat(tipo.substring(1))
    return tipo.replace(/_/g, ' ')
}
