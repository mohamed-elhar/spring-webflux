package org.group.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.group.dto.NAPTRQueryDto;
import org.group.config.CacheConfig;
import org.group.dto.DnsServerDto;
import org.group.dto.NAPTRRecordData;
import org.group.dto.ResErrorQuery;
import org.group.exception.NAPTRQueryException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.xbill.DNS.*;
import reactor.core.publisher.Mono;

import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class NAPTRQueryService {
    private static final Logger LOGGER = LogManager.getLogger(NAPTRQueryService.class);

    @Value("${remote.dns.server.port}")
    private Integer dnsServerPort;

    @Value("${response.delay.ms}")
    private Integer responseDelay;

    public Mono<NAPTRRecordData> prepareNAPTRQuery(NAPTRQueryDto naptrQueryDto) {
        long startTime = System.currentTimeMillis();
        LOGGER.debug("Request parameters: {}", naptrQueryDto);
        NAPTRRecordData naptrRecordData = null;
        for (DnsServerDto dnsServer : naptrQueryDto.getDnsServers()) {
            try {
                ExtendedResolver resolver = new ExtendedResolver();
                Stream.of(resolver.getResolvers()).forEach(r -> resolver.deleteResolver(r));
                resolver.setRetries(1);
                resolver.addResolver(new SimpleResolver(dnsServer.getIpAddress()));
                resolver.setPort(dnsServerPort);
                resolver.setTimeout(Duration.ofMillis(dnsServer.getTimeout()));
                Lookup.setDefaultResolver(resolver);
                Lookup.setDefaultSearchPath(new Name[0]);
                LOGGER.debug("Sending NAPTR query to dns server : [{}:{}]", dnsServer.getIpAddress(), dnsServerPort);
                StringBuilder NAPTRQuery = new StringBuilder(reverseCalled(naptrQueryDto.getCalled().trim()));
                NAPTRQuery.append(dnsServer.getZone()).append(".");
                LOGGER.debug("NAPTR query : [{}]", NAPTRQuery);
                naptrRecordData = sendNAPTRQuery(NAPTRQuery.toString(), naptrQueryDto.getReqId());
                break;
            } catch (UnknownHostException ex) {
                LOGGER.warn("unknown host [{}:{}] - reason : [{}]", dnsServer.getIpAddress(), dnsServerPort, ex.getMessage());
            } catch (TextParseException ex) {
                LOGGER.warn("Could not parse query/response - reason : [{}]", ex.getMessage());
            } catch (NAPTRQueryException ex) {
                LOGGER.warn("No answer was retrieved from dns server - reason : [{}]", ex.getMessage());
            } catch (InterruptedException ex) {
                LOGGER.warn("Timeout of [{} millis] is reached - reason : [{}]", dnsServer.getTimeout(), ex.getMessage());
            }
        }
        long elapsedTime = System.currentTimeMillis() - startTime;
        if(responseDelay.longValue() < elapsedTime) {
            LOGGER.error("ReqID: [{}]-execution time: [{} millis]", naptrQueryDto.getReqId(),elapsedTime);
        } else {
            LOGGER.info("ReqID: [{}]-execution time: [{} millis]", naptrQueryDto.getReqId(),elapsedTime);
        }
        return Mono.justOrEmpty(naptrRecordData);
    }

    @Cacheable(
            value = CacheConfig.CACHE_NAME,
            key = "#NAPTRQuery")
    public NAPTRRecordData sendNAPTRQuery(String NAPTRQuery, String reqId) throws TextParseException, InterruptedException {
        Lookup lookup = new Lookup(NAPTRQuery, Type.NAPTR, DClass.IN);
        Record[] records = lookup.run();
        if (lookup.getResult() == Lookup.SUCCESSFUL) {
            NAPTRRecordData naptrRecord = Arrays.stream(records)
                    .map(this::mapRecordToRecordData)
                    .sorted(Comparator.comparing(NAPTRRecordData::getOrder).thenComparing(NAPTRRecordData::getPreference))
                    .limit(1)
                    .collect(Collectors.toList())
                    .get(0);
            naptrRecord.setResCode(lookup.getResult());
            LOGGER.debug("Request ID : [{}] , Enum response : [{}]", reqId, naptrRecord);
            return naptrRecord;
        } else {
            throw new NAPTRQueryException(new ResErrorQuery(lookup.getResult(), lookup.getErrorString()));
        }
    }

    private static String reverseCalled(String called) {
        String reverseCalled = new StringBuilder(called).reverse().toString();
        char[] reverseChars = reverseCalled.toCharArray();
        StringBuilder result = new StringBuilder();
        for (char c : reverseChars) {
            result.append(c).append('.');
        }
        return result.toString();
    }

    private NAPTRRecordData mapRecordToRecordData(Record record) {
        NAPTRRecord naptrRecord = (NAPTRRecord) record;
        return new NAPTRRecordData(naptrRecord.getOrder(), naptrRecord.getPreference(), naptrRecord.getService(), naptrRecord.getRegexp());
    }
}
