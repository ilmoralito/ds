<g:applyLayout name="${layout}">
    <head>
        <title>Actividades</title>
        <g:set var="mainStyle" value="bootstrap-css, bootstrap-responsive-css, bootstrap-dropdown, jquery-ui, datepicker, app, activity"/>
        <g:set var="activityStyle" value="bootstrap-css, app"/>
        <r:require modules = "${!session?.user ? activityStyle : mainStyle}"/>
    </head>

    <content tag="main">
        <g:if test="${requests || session?.user?.role in allowedUsers}">
            <p class="activity-heading">${requests.size()} actividades ${dateSelected}</p>

            <table class="table table-bordered" style="table-layout: fixed;">
                <thead>
                    <th width="10">B</th>
                    <g:each in="${1..datashows}" var="datashow">
                        <th style="text-align: center; font-weight: normal;">${datashow}</th>
                    </g:each>
                </thead>
                <tbody>
                    <g:each in="${0..blocks}" var="block">
                        <tr>
                            <td class="blocks" style="vertical-align: middle;">
                                <g:if test="${blocks == 6}">
                                    ${block == 3 ? "" : block + 1}
                                </g:if>
                                <g:else>
                                    ${block + 1}
                                </g:else>
                            </td>

                            <g:each in="${1..datashows}" var="d">
                                <g:set var="req" value="${requests.find { it.datashow == d && block in it.hours.block }}"/>
                                <g:set var="blks" value="${req?.hours?.block?.sort()}"/>
                                <g:set var="toName" value="${req && block > blks?.getAt(0)}"/>
                                <g:set var="justAdded" value="${d == params.int('datashow') && block == params.int('block') ? 'justAdded' : ''}"/>
                                <g:set var="currentUser" value="${session?.user && session?.user?.email == req?.user?.email && !justAdded ? 'currentUser' : ''}"/>
                                <!--if current request has details and there is a logged user and user role is admin-->
                                <g:set var="hasDetails" value="${req?.hasDetails() && session?.user?.role == 'admin'}"/>

                                <td id="${req?.id}" class="activity requestInfoContainer ${toName  ? 'toName' : ''} ${justAdded} ${currentUser} ${hasDetails ? 'hasDetails' : ''}" data-datashow="${d}" data-block="${block}" data-doa="${dateSelected}">
                                    <g:if test="${req}">
                                        <g:if test="${!toName}">

                                            <g:if test="${session?.user?.role == 'admin'}">
                                                <div class="btn-group" style="position:absolute; top:1; right:3%;">
                                                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                                                        <span class="caret"></span>
                                                    </a>
                                                    <ul class="dropdown-menu">
                                                        <li>
                                                            <g:link action="updateStatus" params="[id:req.id, path:actionName, status:'attended', dateSelected:dateSelected]">
                                                                Atendido
                                                            </g:link>
                                                        </li>
                                                        <li>
                                                            <g:link action="updateStatus" params="[id:req.id, path:actionName, status:'absent', dateSelected:dateSelected]">
                                                                Ausente
                                                            </g:link>
                                                        </li>
                                                        <li>
                                                            <g:link action="updateStatus" params="[id:req.id, path:actionName, status:'canceled', dateSelected:dateSelected]">
                                                                Cancelado
                                                            </g:link>
                                                        </li>
                                                        <g:if test="${hasDetails}">
                                                            <li class="divider"></li>
                                                            <li>
                                                                <a
                                                                    href="#myModal"
                                                                    class="details"
                                                                    id="details"
                                                                    role="button"
                                                                    data-toggle="modal"
                                                                    data-description="${req?.description}"
                                                                    data-audio="${req?.audio}"
                                                                    data-screen="${req?.screen}"
                                                                    data-internet="${req?.internet}"
                                                                    data-user="${req?.user?.fullName}"
                                                                    data-classroom="${req?.classroom}">
                                                                    Detalle
                                                                </a>
                                                            </li>
                                                        </g:if>
                                                    </ul>
                                                </div>
                                            </g:if>

                                            <p>${req.user.fullName}</p>
                                            <p>${req.school}</p>
                                            <p><ds:classroom room="${req.classroom}"/></p>
                                        </g:if>
                                    </g:if>
                                </td>
                            </g:each>
                        </tr>
                    </g:each>
                </tbody>
            </table>
        </g:if>
        <g:else>
            <p>No hay actividades programadas ${dateSelected}!</p>
        </g:else>

        <div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h4>Por <span id="user"></span> en <span id="classroom"></span></h4>
          </div>
          <div class="modal-body">
            <table class="table no-top-border">
                <tbody>
                    <tr class="hide">
                        <td width="1">Internet</td>
                        <td id="internet"></td>
                    </tr>
                    <tr class="hide">
                        <td width="1">Parlantes</td>
                        <td id="audio"></td>
                    </tr>
                    <tr class="hide">
                        <td width="1">Pantalla</td>
                        <td id="screen"></td>
                    </tr>
                    <tr class="hide">
                        <td width="1">Comentario</td>
                        <td id="description"></td>
                    </tr>
                </tbody>
            </table>
          </div>
        </div>
    </content>

    <content tag="col1">
        <g:if test="${layout != 'oneColumn'}">
            <p class="activity-heading">Filtrar por</p>
            <g:form action="activity" autocomplete="off">
                <g:hiddenField name="dateSelected" value="${dateSelected}"/>
                <g:render template="filter"/>

                <g:submitButton name="submit" value="Filtrar" class="btn btn-primary btn-block filter-button"/>
            </g:form>
        </g:if>
    </content>
</g:applyLayout>
