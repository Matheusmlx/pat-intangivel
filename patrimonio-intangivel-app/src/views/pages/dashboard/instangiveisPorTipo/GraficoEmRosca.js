import {Doughnut} from 'vue-chartjs'
import ChartJSPluginDatalabels from 'chartjs-plugin-datalabels'
import _ from 'lodash'

let router
let filtros
let dadosDoGrafico

export default {
    extends: Doughnut,
    props: ['dados'],
    data() {
        return {
            intangivelClicado: null,
            datacollection: {
                hoverBackgroundColor: 'red',
                hoverBorderWidth: 10,
                labels: this.buscarLabels(),
                datasets: [
                    {
                        backgroundColor: ["#e5b036", "#d1753b", "#6fb165", "#55a2ce", "#d64fc6", "#65dbf0"],
                        borderWidth: 0,
                        weight: 2,
                        data: this.buscarData()
                    }
                ]
            },
            options: {
                legend: {
                    position: 'right',
                    display: true,
                    labels:{
                        boxWidth:12,
                        fontSize:10,
                        padding:6,
                    }
                },
                tooltips: {
                    enabled: false
                },
                animation: {
                    animateScale: true,
                    animateRotate: true
                },
                pieceLabel: {
                    mode: 'value'
                },
                onHover: function (e) {
                    let point = this.getElementAtEvent(e);
                    if (point.length) e.target.style.cursor = 'pointer';
                    else e.target.style.cursor = 'default';
                },
                onClick: function (mouseEvent, chartElement) {
                    let elemento = chartElement[0]
                    let nome = this.data.labels[elemento._index]

                    if (nome === 'Direitos Autorais') {
                        nome = 'DIREITOS_AUTORAIS'
                    }
                    if (nome === 'Títulos de Publicação') {
                        nome = 'TITULOS_DE_PUBLICACAO'
                    }

                    if (nome === 'Receitas, Fórmulas e Projetos') {
                        nome = 'RECEITAS_FORMULAS_PROJETOS'
                    }

                    filtros.conteudo.value = nome
                    router.push({name: 'TodosPatrimonios'})
                },
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    unregister: (ChartJSPluginDatalabels),
                    datalabels: {
                        color: 'white',
                        textAlign: 'center',
                        font: {
                            weight: 'bold',
                            size: 12
                        },
                        display: context => context.dataset.data[context.dataIndex] !== 0
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
                labels.push(item.nome)
            }
            return labels
        },
        buscarData() {
            let totalPorTipo = []
            for (let item of this.dados) {
                totalPorTipo.push(item.quantidade)
            }
            return totalPorTipo
        },
        setarDadosDoGrafico() {
            filtros = this.$store.state.patrimonio.todosPatrimonios.filtros
            router = this.$router
            dadosDoGrafico = _.cloneDeep(this.dados)
        }
    }
}
