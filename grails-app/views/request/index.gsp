<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="layout" content="BaseLayout"/>
        <title>Solicitudes del dia</title>
        <r:require modules = "bootstrap-css, bootstrap-modal, base"/>
    </head>

    <body>
        <div class="span12">
            ${requestList}
            ${datashows}

            <table class="table table-bordered">
                <caption>${requestListCount} resultados hoy</caption>

                <thead>
                    <tr>
                        <g:each in="${1..datashows}" var="datashow" status="index">
                            <g:if test="${index == 0}">
                                <th width="15"></th>
                            </g:if>

                            <th>${datashow}</th>
                        </g:each>
                    </tr>
                </thead>

                <tbody>
                    <g:each in="${1..7}" var="block">
                        <tr>
                            <g:if test="${block == 4}">
                                <td class="datashow">Medio dia</td>
                            </g:if>
                            <g:else>
                                <td class="datashow">${block}</td>
                            </g:else>

                            <g:each in="${1..datashows}" var="datashow">
                                <g:set var="activity" value="${requestList.find { it.datashow == datashow && block in it.blocks.tokenize(',')*.toInteger() }}"/>

                                <g:if test="${activity}">
                                    <td rowspan="${activity.blocks.tokenize(',').size() - 1 ?: 0}" width="50" height="50">
                                        ${activity.fullName}
                                        ${activity.classroom}
                                    </td>
                                </g:if>
                                <g:else>
                                    <td width="50" height="50"></td>
                                </g:else>
                            </g:each>
                        </tr>
                    </g:each>
                </tbody>
            </table>

            <table class="table table-bordered">
                <tr>
                    <th></th>
                    <th>1</th>
                    <th>2</th>
                    <th>3</th>
                    <th>4</th>
                    <th>5</th>
                    <th>6</th>
                    <th>7</th>
                    <th>8</th>
                    <th>9</th>
                    <th>10</th>
                    <th>11</th>
                    <th>12</th>
                </tr>

                <tr>
                    <th>1</th>
                    <td rowspan="7"></td>
                    <td rowspan="2"></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td rowspan="4"></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>

                <tr>
                    <th>2</th>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>

                <tr>
                    <th>3</th>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>

                <tr>
                    <th>4</th>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>

                <tr>
                    <th>5</th>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>

                <tr>
                    <th>6</th>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>

                <tr>
                    <th>7</th>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
            </table>

            <ds:flashMessage>${flash.message}</ds:flashMessage>
        </div>
    </body>
</html>
